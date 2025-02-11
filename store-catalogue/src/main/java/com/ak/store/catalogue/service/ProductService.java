package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.*;
import com.ak.store.catalogue.model.pojo.ProcessedProductImages;
import com.ak.store.catalogue.repository.ProductRepo;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.catalogue.service.product.PriceCalculator;
import com.ak.store.catalogue.validator.ProductImageValidator;
import com.ak.store.common.dto.catalogue.ProductImageWriteDTO;
import com.ak.store.common.dto.catalogue.ProductWriteDTO;
import com.ak.store.common.dto.search.nested.SortingType;
import com.ak.store.common.payload.product.ProductWritePayload;
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

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int BATCH_SIZE;

    @Transactional
    public Product findOneProductWithAll(Long id) {
        Product product = productRepo.findOneWithCharacteristicsAndCategoryById(id)
                .orElseThrow(() -> new RuntimeException("product with id %s didnt find".formatted(id)));

        product.getImages().size();
        return product;
    }

    public Product findOneProductWithCharacteristics(Long id) {
        return productRepo.findOneWithCharacteristicsAndCategoryById(id)
                .orElseThrow(() -> new RuntimeException("product with id %s didnt find".formatted(id)));
    }

    public ProcessedProductImages saveOrUpdateAllImage(ProductImageWriteDTO productImageDTO) {
        Product updatedProduct = productRepo.findOneWithImagesById(productImageDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("product with id %s didnt find".formatted(productImageDTO.getProductId())));

        productImageValidator.validate(productImageDTO, updatedProduct.getImages());

        ProcessedProductImages processedProductImages = processProductImages(productImageDTO, updatedProduct);

        updatedProduct.getImages().clear();
        updatedProduct.getImages().addAll(processedProductImages.getNewProductImages());
        productRepo.saveAndFlush(updatedProduct);

        return processedProductImages;
    }

    public List<Product> findAllProductView(List<Long> ids) {
        return productRepo.findAllWithImagesByIdIn(ids);
    }

    public List<Product> findAllProductView(List<Long> ids, SortingType sortingType) {
        return productRepo.findAllWithImagesByIdIn(ids, getSort(sortingType));
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

    public Product deleteOneProduct(Long id) {
        Product product = productRepo.findOneWithImagesById(id).orElseThrow(() -> new RuntimeException("no products found"));
        productRepo.deleteById(id);
        return product;
    }

    public Product createOneProduct(ProductWritePayload productPayload) {
        Product createdProduct = catalogueMapper.mapToProduct(productPayload.getProduct());
        if (createdProduct.getCategory() == null || createdProduct.getCategory().getId() == null) {
            throw new RuntimeException("category_id is null");
        }
        productCharacteristicService.createProductCharacteristics(createdProduct, productPayload.getCreateCharacteristics());

        //flush for immediate validation, without it, data will index in ES, even when validation failed
        productRepo.saveAndFlush(createdProduct);

        return createdProduct;
    }

    public List<Product> createAllProduct(List<ProductWritePayload> productPayloads) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < productPayloads.size(); i++) {
            if (i > 0 && i % BATCH_SIZE == 0) {
                productRepo.saveAllAndFlush(products);
                productRepo.clear();
            }

            Product createdProduct = catalogueMapper.mapToProduct(productPayloads.get(i).getProduct());
            if (createdProduct.getCategory() == null || createdProduct.getCategory().getId() == null) {
                throw new RuntimeException("one of the products does not have a defined category_id");
            }
            productCharacteristicService.createProductCharacteristics(createdProduct, productPayloads.get(i).getCreateCharacteristics());
            products.add(createdProduct);
        }

        //flush for immediate validation, without it, data will index in ES, even when validation failed
        productRepo.saveAllAndFlush(products);
        productRepo.clear();
        return products;
    }

    public Product updateOneProduct(ProductWritePayload productPayload, Long productId) {
        Product updatedProduct = productRepo.findOneWithCharacteristicsAndCategoryById(productId)
                .orElseThrow(() -> new RuntimeException("product with id %s didnt find".formatted(productId)));

        updateProduct(updatedProduct, productPayload.getProduct());

        productCharacteristicService.createProductCharacteristics(updatedProduct, productPayload.getCreateCharacteristics());
        productCharacteristicService.updateProductCharacteristics(updatedProduct, productPayload.getUpdateCharacteristics());
        productCharacteristicService.deleteProductCharacteristics(updatedProduct, productPayload.getDeleteCharacteristics());

        //flush for immediate validation, without it, data will index in ES, even when validation failed
        productRepo.saveAndFlush(updatedProduct);

        return updatedProduct;
    }

    private void updateProduct(Product updatedProduct, ProductWriteDTO productWriteDTO) {
        if (productWriteDTO.getTitle() != null) {
            updatedProduct.setTitle(productWriteDTO.getTitle());
        }

        if (productWriteDTO.getDescription() != null) {
            updatedProduct.setDescription(productWriteDTO.getDescription());
        }

        PriceCalculator.updatePrice(updatedProduct, productWriteDTO);

        if (productWriteDTO.getCategoryId() != null
                && !updatedProduct.getCategory().getId().equals(productWriteDTO.getCategoryId())) {
            updatedProduct.setCategory(
                    Category.builder()
                            .id(productWriteDTO.getCategoryId())
                            .build()
            );
            updatedProduct.getCharacteristics().clear();
        }
    }
}
