package com.ak.store.catalogue.utils;

import com.ak.store.catalogue.model.entity.ne.Category;
import com.ak.store.catalogue.model.entity.ne.Characteristic;
import com.ak.store.catalogue.model.entity.ne.Product;
import com.ak.store.catalogue.model.entity.ne.TextValue;
import com.ak.store.common.dto.catalogue.product.*;
import com.ak.store.common.payload.product.ProductWritePayload;
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

    public ProductFullReadDTO mapToProductReadDTO(com.ak.store.catalogue.model.entity.Product product) {
        return modelMapper.map(product, ProductFullReadDTO.class);
    }

    public Product mapToProduct(ProductWritePayload productWritePayload) {
        ProductWriteDTO productWriteDTO = productWritePayload.getProduct();
        Product product = modelMapper.map(productWriteDTO, Product.class);
        product.setGrade(2);
        product.setCategory(
                Category.builder()
                        .id(productWriteDTO.getCategoryId())
                        .build());

        return product;
    }

    public ProductViewReadDTO mapToProductViewReadDTO(com.ak.store.catalogue.model.entity.ne.Product product) {
        return modelMapper.map(product, ProductViewReadDTO.class);
    }

    public ProductFullReadDTO mapToProductFullReadDTO(com.ak.store.catalogue.model.entity.ne.Product product) {
        return modelMapper.map(product, ProductFullReadDTO.class);
    }

    public AvailableFilterValuesDTO mapToAvailableFilterValuesDTO(Characteristic characteristic) {
        List<String> textValues = characteristic.getTextValues().stream().map(TextValue::getTextValue).toList();
        characteristic.setTextValues(null);
        var characteristicDTO = modelMapper.map(characteristic, AvailableFilterValuesDTO.class);
        characteristicDTO.setTextValues(textValues);
        return characteristicDTO;
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
