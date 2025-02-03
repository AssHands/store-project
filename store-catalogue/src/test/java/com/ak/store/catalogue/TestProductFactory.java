package com.ak.store.catalogue;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.ProductCharacteristic;
import com.ak.store.common.dto.catalogue.product.ProductCharacteristicDTO;
import com.ak.store.common.dto.catalogue.product.ProductWriteDTO;
import com.ak.store.common.payload.product.ProductWritePayload;

public class TestProductFactory {

    public static ProductCharacteristicDTO createProductCharacteristicDTO(long id, String name, int numericValue) {
        return ProductCharacteristicDTO.builder().id(id).name(name).numericValue(numericValue).build();
    }

    public static ProductCharacteristicDTO createProductCharacteristicDTO(long id, String name, String textValue) {
        return ProductCharacteristicDTO.builder().id(id).name(name).textValue(textValue).build();
    }

    public static ProductCharacteristicDTO createProductCharacteristicDTO(long id, String textValue) {
        return ProductCharacteristicDTO.builder().id(id).textValue(textValue).build();
    }

    public static ProductCharacteristicDTO createProductCharacteristicDTO(long id, int numericValue) {
        return ProductCharacteristicDTO.builder().id(id).numericValue(numericValue).build();
    }

    public static ProductCharacteristic createProductCharacteristic(long characteristicId, String name, int numericValue, long productId) {
        return ProductCharacteristic.builder()
                .product(Product.builder().id(productId).build())
                .characteristic(Characteristic.builder().id(characteristicId).name(name).build())
                .numericValue(numericValue)
                .build();
    }

    public static ProductCharacteristic createProductCharacteristic(long characteristicId, String name, String textValue, long productId) {
        return ProductCharacteristic.builder()
                .product(Product.builder().id(productId).build())
                .characteristic(Characteristic.builder().id(characteristicId).name(name).build())
                .textValue(textValue)
                .build();
    }

    public static ProductWritePayload createProductPayload(String title, String description, int fullPrice,
                                                           int discountPercentage, long categoryId) {
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

    public static Product createProductWithCategory(long categoryId) {
        return Product.builder().category(Category.builder().id(categoryId).build()).build();
    }
}
