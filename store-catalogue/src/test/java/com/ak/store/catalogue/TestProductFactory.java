package com.ak.store.catalogue;

import com.ak.store.catalogue.model.entity.*;
import com.ak.store.common.model.catalogue.form.ProductCharacteristicForm;
import com.ak.store.common.model.catalogue.form.ProductForm;
import com.ak.store.common.payload.catalogue.ProductWritePayload;

import java.util.List;

public class TestProductFactory {
    public static ProductWritePayload createProductPayload(String title, String description, Integer fullPrice,
                                                           Integer discountPercentage, Long categoryId) {
        return ProductWritePayload.builder()
                .product(ProductForm.builder()
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

    public static Product createProduct(Long id, Long categoryId, List<ProductCharacteristic> productCharacteristics) {
        return Product.builder()
                .id(id)
                .category(Category.builder().id(categoryId).build())
                .characteristics(productCharacteristics)
                .build();
    }

    public static ProductCharacteristic createProductCharacteristic(Long characteristicId, String textValue) {
        return ProductCharacteristic.builder()
                .characteristic(Characteristic.builder().id(characteristicId).build())
                .textValue(textValue)
                .build();
    }

    public static ProductCharacteristic createProductCharacteristic(Long characteristicId, Integer numericValue) {
        return ProductCharacteristic.builder()
                .characteristic(Characteristic.builder().id(characteristicId).build())
                .numericValue(numericValue)
                .build();
    }

    public static ProductImage createProductImage(Integer index, String key) {
        return ProductImage.builder()
                .index(index)
                .imageKey(key)
                .build();
    }

    public static ProductCharacteristicForm createProductCharacteristicDTO(Long characteristicId, String textValue) {
        return ProductCharacteristicForm.builder()
                .id(characteristicId)
                .textValue(textValue)
                .build();
    }

    public static ProductCharacteristicForm createProductCharacteristicDTO(Long characteristicId, Integer numericValue) {
        return ProductCharacteristicForm.builder()
                .id(characteristicId)
                .numericValue(numericValue)
                .build();
    }

    public static ProductCharacteristicForm createProductCharacteristicDTO(Long characteristicId, Integer numericValue, String textValue) {
        return ProductCharacteristicForm.builder()
                .id(characteristicId)
                .numericValue(numericValue)
                .textValue(textValue)
                .build();
    }
}
