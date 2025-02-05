package com.ak.store.catalogue;

import com.ak.store.catalogue.model.entity.*;
import com.ak.store.common.dto.catalogue.product.ProductCharacteristicDTO;
import com.ak.store.common.dto.catalogue.product.ProductWriteDTO;
import com.ak.store.common.payload.product.ProductWritePayload;

import java.util.Arrays;

public class TestProductFactory {
    public static ProductWritePayload createProductPayload(String title, String description, Integer fullPrice,
                                                           Integer discountPercentage, Long categoryId) {
        return ProductWritePayload.builder()
                .product(ProductWriteDTO.builder()
                        .title(title)
                        .description(description)
                        .fullPrice(fullPrice)
                        .discountPercentage(discountPercentage)
                        .categoryId(categoryId)
                        .build())
                .build();
    }

    public static Product createProductWithCategory(Long categoryId) {
        return Product.builder().category(Category.builder().id(categoryId).build()).build();
    }

    public static Product createProduct(Long id, String title, String description,
                                        Integer fullPrice, Integer discountPercentage, Integer currentPrice, Long categoryId) {
        return Product.builder()
                .id(id)
                .title(title)
                .description(description)
                .fullPrice(fullPrice)
                .discountPercentage(discountPercentage)
                .currentPrice(currentPrice)
                .category(Category.builder().id(categoryId).build())
                .build();
    }

    public static ProductImage createProductImage(Integer index, String key) {
        return ProductImage.builder()
                .index(index)
                .imageKey(key)
                .build();
    }

//    public static ProductCharacteristicDTO createProductCharacteristicDTO() {
//        ProductCharacteristicDTO.builder()
//
//    }
}
