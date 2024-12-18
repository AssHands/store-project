package com.ak.store.catalogue.utils;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.TextValue;
import com.ak.store.catalogue.model.entity.relation.ProductCharacteristic;
import com.ak.store.common.dto.catalogue.product.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CatalogueMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public CatalogueMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Product mapToProduct(ProductWriteDTO productWriteDTO) {
        Product product = modelMapper.map(productWriteDTO, Product.class);
        product.setGrade(2);
        product.setCategory(
                Category.builder()
                        .id(productWriteDTO.getCategoryId())
                        .build());

        return product;
    }

    public ProductViewReadDTO mapToProductViewReadDTO(Product product) {
        return modelMapper.map(product, ProductViewReadDTO.class);
    }

    public ProductFullReadDTO mapToProductFullReadDTO(Product product) {
        return modelMapper.map(product, ProductFullReadDTO.class);
    }

    public AvailableFilterValuesDTO mapToAvailableFilterValuesDTO(Characteristic characteristic) {
        List<String> textValues = characteristic.getTextValues().stream().map(TextValue::getTextValue).toList();
        characteristic.setTextValues(null);
        var characteristicDTO = modelMapper.map(characteristic, AvailableFilterValuesDTO.class);
        characteristicDTO.setTextValues(textValues);
        return characteristicDTO;
    }

    public ProductCharacteristic mapToProductCharacteristic(CharacteristicWriteDTO characteristicWriteDTO, Product product) {
        var productCharacteristic = ProductCharacteristic.builder().numericValue(characteristicWriteDTO.getNumericValue()).textValue(characteristicWriteDTO.getTextValue()).build();

        productCharacteristic.setProduct(product);
        productCharacteristic.setCharacteristic(Characteristic.builder().id(characteristicWriteDTO.getId()).build());
        return productCharacteristic;
    }

    public CategoryDTO mapToCategoryDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }

    public AvailableCharacteristicValuesDTO mapToAvailableCharacteristicValuesDTO(Characteristic characteristic) {
        List<String> textValues = characteristic.getTextValues().stream().map(TextValue::getTextValue).toList();
        characteristic.setTextValues(null);
        var characteristicDTO = modelMapper.map(characteristic, AvailableCharacteristicValuesDTO.class);
        characteristicDTO.setTextValues(textValues);
        return characteristicDTO;
    }
}
