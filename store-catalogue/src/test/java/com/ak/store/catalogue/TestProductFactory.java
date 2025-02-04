package com.ak.store.catalogue;

import com.ak.store.catalogue.model.entity.*;
import com.ak.store.common.dto.catalogue.product.ProductCharacteristicDTO;
import com.ak.store.common.dto.catalogue.product.ProductWriteDTO;
import com.ak.store.common.payload.product.ProductWritePayload;

public class TestProductFactory {

    public static ProductCharacteristicDTO createProductCharacteristicDTO(Long id, String name, Integer numericValue) {
        return ProductCharacteristicDTO.builder().id(id).name(name).numericValue(numericValue).build();
    }

    public static ProductCharacteristicDTO createProductCharacteristicDTO(Long id, String name, String textValue) {
        return ProductCharacteristicDTO.builder().id(id).name(name).textValue(textValue).build();
    }

    public static ProductCharacteristicDTO createProductCharacteristicDTO(Long id, String textValue) {
        return ProductCharacteristicDTO.builder().id(id).textValue(textValue).build();
    }

    public static ProductCharacteristicDTO createProductCharacteristicDTO(Long id, Integer numericValue) {
        return ProductCharacteristicDTO.builder().id(id).numericValue(numericValue).build();
    }

    public static ProductCharacteristic createProductCharacteristic(Long characteristicId, String name, Integer numericValue, Long productId) {
        return ProductCharacteristic.builder()
                .product(Product.builder().id(productId).build())
                .characteristic(Characteristic.builder().id(characteristicId).name(name).build())
                .numericValue(numericValue)
                .build();
    }

    public static ProductCharacteristic createProductCharacteristic(Long characteristicId, String name, String textValue, Long productId) {
        return ProductCharacteristic.builder()
                .product(Product.builder().id(productId).build())
                .characteristic(Characteristic.builder().id(characteristicId).name(name).build())
                .textValue(textValue)
                .build();
    }

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

    public static ProductWritePayload createProductPayload() {
        return ProductWritePayload.builder().build();
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

    public static ProductImage createProductImage(Long id, Integer index, String key, Long productId) {
        return ProductImage.builder()
                .id(id)
                .index(index)
                .imageKey(key)
                .product(Product.builder().id(productId).build())
                .build();
    }

    public static ProductImage createProductImage(Integer index, String key, Long productId) {
        return ProductImage.builder()
                .index(index)
                .imageKey(key)
                .product(Product.builder().id(productId).build())
                .build();
    }
}
