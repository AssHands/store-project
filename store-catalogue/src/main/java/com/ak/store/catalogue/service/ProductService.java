package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.*;
import com.ak.store.catalogue.model.pojo.ProcessedProductImages;
import com.ak.store.catalogue.repository.ProductRepo;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.catalogue.service.product.PriceCalculator;
import com.ak.store.catalogue.validator.business.ProductBusinessValidator;
import com.ak.store.catalogue.validator.ProductImageValidator;
import com.ak.store.common.model.catalogue.dto.ImageDTO;
import com.ak.store.common.model.catalogue.dto.ProductDTO;
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
    private final CatalogueMapper catalogueMapper;
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

    public ProcessedProductImages saveOrUpdateAllImage(ImageDTO imageDTO) {
        Product updatedProduct = findOneWithImages(imageDTO.getProductId());
        productImageValidator.validate(imageDTO, updatedProduct.getImages());
        ProcessedProductImages processedProductImages = processProductImages(imageDTO, updatedProduct);

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
        productRepo.deleteById(id);
        return product;
    }

    public Product createOne(ProductWritePayload productPayload) {
        productBusinessValidator.validateCreation(productPayload.getProduct());
        Product createdProduct = catalogueMapper.mapToProduct(productPayload.getProduct());
        productCharacteristicService.createAll(createdProduct, productPayload.getCreateCharacteristics());

        //flush for immediate validation
        productRepo.saveAndFlush(createdProduct);
        return createdProduct;
    }

    public List<Product> createAll(List<ProductWritePayload> productPayloads) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < productPayloads.size(); i++) {
            if (i > 0 && i % BATCH_SIZE == 0) {
                productRepo.saveAllAndFlush(products);
                productRepo.clear();
            }

            productBusinessValidator.validateCreation(productPayloads.get(i).getProduct());
            Product createdProduct = catalogueMapper.mapToProduct(productPayloads.get(i).getProduct());
            productCharacteristicService.createAll(createdProduct, productPayloads.get(i).getCreateCharacteristics());
            products.add(createdProduct);
        }

        //flush for immediate validation
        productRepo.saveAllAndFlush(products);
        productRepo.clear();
        return products;
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

    private void updateProduct(Product updatedProduct, ProductDTO productDTO) {
        PriceCalculator.updatePrice(updatedProduct, productDTO);
        if (productDTO.getTitle() != null) {
            updatedProduct.setTitle(productDTO.getTitle());
        }
        if (productDTO.getDescription() != null) {
            updatedProduct.setDescription(productDTO.getDescription());
        }
        if(productDTO.getIsAvailable() != null) {
            updatedProduct.setIsAvailable(productDTO.getIsAvailable());
        }
        if (productDTO.getCategoryId() != null
                && !updatedProduct.getCategory().getId().equals(productDTO.getCategoryId())) {
            updatedProduct.setCategory(
                    Category.builder()
                            .id(productDTO.getCategoryId())
                            .build()
            );
            updatedProduct.getCharacteristics().clear();
        }
    }

    public Boolean existOne(Long id) {
        return productRepo.existsById(id);
    }

    public Boolean availableOne(Long id) {
        return productRepo.existsOneByIdAndIsAvailableIsTrue(id);
    }

    public Boolean availableAll(List<Long> ids) {
        return productRepo.countByIdInAndIsAvailableIsTrue(ids) == ids.size();
    }

    public List<Product> findAll(List<Long> ids) {
        return productRepo.findAllById(ids);
    }
}
