package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.dto.write.ProductWriteDTO;
import com.ak.store.catalogue.model.dto.ProductDTOnew;
import com.ak.store.catalogue.model.entity.*;
import com.ak.store.catalogue.model.pojo.ProcessedProductImages;
import com.ak.store.catalogue.repository.ProductRepo;
import com.ak.store.catalogue.service.product.PriceCalculator;
import com.ak.store.catalogue.util.mapper.ProductMapper;
import com.ak.store.catalogue.validator.service.ProductServiceValidator;
import com.ak.store.catalogue.validator.ProductImageValidator;
import com.ak.store.common.model.catalogue.form.ImageForm;
import com.ak.store.common.model.catalogue.form.ProductForm;
import com.ak.store.common.model.search.common.SortingType;
import com.ak.store.common.payload.catalogue.ProductWritePayload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ak.store.catalogue.util.ProductImageProcessor.processProductImages;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final ProductImageValidator productImageValidator;
    private final ProductCharacteristicService productCharacteristicService;
    private final CategoryService categoryService;
    private final ProductServiceValidator productServiceValidator;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int BATCH_SIZE;

    @Transactional
    public Product findOneWithAll(Long id) {
        Product product = productRepo.findOneWithCharacteristicsAndCategoryById(id)
                .orElseThrow(() -> new RuntimeException("product with id %s didnt find".formatted(id)));

        product.getImages().size();
        return product;
    }

    public Product findOneWithCharacteristicsAndCategory(Long id) {
        return productRepo.findOneWithCharacteristicsAndCategoryById(id)
                .orElseThrow(() -> new RuntimeException("product with id %s didnt find".formatted(id)));
    }

    public Product findOneWithImages(Long id) {
        return productRepo.findOneWithImagesById(id)
                .orElseThrow(() -> new RuntimeException("product with id %s didnt find".formatted(id)));
    }

    public ProcessedProductImages saveOrUpdateAllImage(ImageForm imageForm) {
        Product updatedProduct = findOneWithImages(imageForm.getProductId());
        productImageValidator.validate(imageForm, updatedProduct.getImages());
        ProcessedProductImages processedProductImages = processProductImages(imageForm, updatedProduct);

        updatedProduct.getImages().clear();
        updatedProduct.getImages().addAll(processedProductImages.getNewImages());
        productRepo.saveAndFlush(updatedProduct);

        return processedProductImages;
    }

    public Boolean existOne(Long id) {
        return productRepo.IsExistOneById(id);
    }

    public Boolean availableOne(Long id) {
        return productRepo.isAvailableOneById(id);
    }

    public Boolean availableAll(List<Long> ids) {
        return productRepo.isAvailableAllByIds(ids, ids.size());
    }

    public List<Product> findAllOld(List<Long> ids) {
        return productRepo.findAllById(ids);
    }

    //-----------------------------

    private Product findOneById(Long id) {
        return productRepo.findById(id).orElseThrow(() -> new RuntimeException("not found"));
    }

    private List<Product> findAllById(List<Long> ids) {
        return productRepo.findAllById(ids);
    }

    public ProductDTOnew findOne(Long id) {
        return productMapper.toProductDTOnew(findOneById(id));
    }

    public List<ProductDTOnew> findAll(List<Long> ids) {
        return productMapper.toProductDTOnew(findAllById(ids));
    }

    public Boolean isExistAll(List<Long> ids) {
        return productRepo.isExistAllByIds(ids, ids.size());
    }

    @Transactional
    public ProductDTOnew createOne(ProductWriteDTO request) {
        productServiceValidator.validateCreation(request);
        Product product = productMapper.toProduct(request);

        PriceCalculator.definePrice(product, request);
        product.setIsDeleted(false);

        //flush for immediate validation
        productRepo.saveAndFlush(product);
        return productMapper.toProductDTOnew(product);
    }

    @Transactional
    public ProductDTOnew updateOne(Long id, ProductWriteDTO request) {
        Product product = findOneById(id);

        updateOneFromDto(product, request);

        //flush for immediate validation
        return productMapper.toProductDTOnew(productRepo.saveAndFlush(product));
    }

    private void updateOneFromDto(Product product, ProductWriteDTO request) {
        PriceCalculator.definePrice(product, request);

        if (request.getTitle() != null) {
            product.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if(request.getIsAvailable() != null) {
            product.setIsAvailable(request.getIsAvailable());
        }
        if (request.getCategoryId() != null && !product.getCategory().getId().equals(request.getCategoryId())) {
            product.setCategory(
                    Category.builder()
                            .id(request.getCategoryId())
                            .build());

            product.getCharacteristics().clear();
        }
    }

    public ProductDTOnew deleteOne(Long id) {
        Product product = findOneById(id);

        product.setIsAvailable(false);
        product.setIsDeleted(true);

        return productMapper.toProductDTOnew(productRepo.save(product));
    }
}
