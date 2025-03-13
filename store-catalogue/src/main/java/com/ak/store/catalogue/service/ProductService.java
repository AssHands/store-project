package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.*;
import com.ak.store.catalogue.model.pojo.ProcessedProductImages;
import com.ak.store.catalogue.repository.ProductRepo;
import com.ak.store.catalogue.util.CatalogueMapper0;
import com.ak.store.catalogue.service.product.PriceCalculator;
import com.ak.store.catalogue.validator.business.ProductBusinessValidator;
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
    private final CatalogueMapper0 catalogueMapper0;
    private final ProductImageValidator productImageValidator;
    private final ProductCharacteristicService productCharacteristicService;

    private final ProductBusinessValidator productBusinessValidator;

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
        updatedProduct.getImages().addAll(processedProductImages.getNewProductImages());
        productRepo.saveAndFlush(updatedProduct);

        return processedProductImages;
    }

    public List<Product> findAllPoor(List<Long> ids, SortingType sortingType) {
        return productRepo.findAllWithImagesByIdIn(ids, getSort(sortingType));
    }

    public List<Product> findAllPoor(List<Long> ids) {
        return productRepo.findAllWithImagesByIdIn(ids);
    }

    private Sort getSort(SortingType sortingType) {
        Sort sort;

        switch (sortingType) {
            case PRICE_UP -> {
                sort = Sort.by(Sort.Direction.ASC, "currentPrice");
            }
            case PRICE_DOWN -> {
                sort = Sort.by(Sort.Direction.DESC, "currentPrice");
            }
            case RATING -> {
                //todo: add amount_reviews
                sort = Sort.by(Sort.Direction.DESC, "grade");
            }
            default -> { //POPULAR todo: сделать сортировку по популярности. данный вариант не работает
                sort = Sort.by(Sort.Direction.DESC, "amount_reviews");
            }
        }

        return sort;
    }

    public Product deleteOne(Long id) {
        //find with images for reduce queries to db
        Product product = findOneWithImages(id);

        product.setIsAvailable(false);
        product.setIsDeleted(true);

        return productRepo.save(product);
    }

    public Product createOne(ProductWritePayload productPayload) {
        productBusinessValidator.validateCreation(productPayload.getProduct());
        Product product = catalogueMapper0.mapToProduct(productPayload.getProduct());
        productCharacteristicService.createAll(product, productPayload.getCreateCharacteristics());

        product.setIsDeleted(false);

        //flush for immediate validation
        productRepo.saveAndFlush(product);
        return product;
    }
    public Product updateOne(ProductWritePayload productPayload, Long productId) {
        Product updatedProduct = findOneWithCharacteristicsAndCategory(productId);

        updateProduct(updatedProduct, productPayload.getProduct());
        productCharacteristicService.createAll(updatedProduct, productPayload.getCreateCharacteristics());
        productCharacteristicService.updateAll(updatedProduct, productPayload.getUpdateCharacteristics());
        productCharacteristicService.deleteAll(updatedProduct, productPayload.getDeleteCharacteristics());

        //flush for immediate validation
        productRepo.saveAndFlush(updatedProduct);

        return updatedProduct;
    }

    private void updateProduct(Product updatedProduct, ProductForm productForm) {
        PriceCalculator.updatePrice(updatedProduct, productForm);
        if (productForm.getTitle() != null) {
            updatedProduct.setTitle(productForm.getTitle());
        }
        if (productForm.getDescription() != null) {
            updatedProduct.setDescription(productForm.getDescription());
        }
        if(productForm.getIsAvailable() != null) {
            updatedProduct.setIsAvailable(productForm.getIsAvailable());
        }
        if (productForm.getCategoryId() != null
                && !updatedProduct.getCategory().getId().equals(productForm.getCategoryId())) {
            updatedProduct.setCategory(
                    Category.builder()
                            .id(productForm.getCategoryId())
                            .build()
            );
            updatedProduct.getCharacteristics().clear();
        }
    }

    public Boolean existOne(Long id) {
        return productRepo.existOneById(id);
    }

    public Boolean availableOne(Long id) {
        return productRepo.availableOneById(id);
    }

    public Boolean availableAll(List<Long> ids) {
        return productRepo.availableAllById(ids, ids.size());
    }

    public List<Product> findAll(List<Long> ids) {
        return productRepo.findAllById(ids);
    }
}
