package com.ak.store.catalogue.utils;

import com.ak.store.catalogue.model.document.ProductCharacteristicDocument;
import com.ak.store.catalogue.model.document.ProductDocument;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.TextValue;
import com.ak.store.catalogue.model.entity.relation.ProductCharacteristic;
import com.ak.store.common.dto.catalogue.product.*;
import com.ak.store.common.dto.search.Filters;
import com.ak.store.common.dto.search.nested.NumericFilter;
import com.ak.store.common.dto.search.nested.TextFilter;
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
        product.setId(null); //todo: delete
        product.setGrade(2); //todo: delete

        if(productWriteDTO.getDiscountPercentage() == null || productWriteDTO.getDiscountPercentage() == 0) {
            product.setDiscountPercentage(0);
            product.setCurrentPrice(product.getFullPrice());
        } else {
            int discount = product.getFullPrice() * product.getDiscountPercentage() / 100;
            int priceWithDiscount = product.getFullPrice() - discount;
            product.setCurrentPrice(priceWithDiscount);
        }

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


    //method required product entity instead of product id cuz product doesn't have id when we just created it, for Hibernate
    public ProductCharacteristic mapToProductCharacteristic(ProductCharacteristicDTO productCharacteristicDTO, Product product) {
        return ProductCharacteristic.builder()
                .numericValue(productCharacteristicDTO.getNumericValue())
                .textValue(productCharacteristicDTO.getTextValue())
                .product(product)
                .characteristic(Characteristic.builder()
                        .id(productCharacteristicDTO.getId())
                        .build())
                .build();
    }

    public CategoryDTO mapToCategoryDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }

    public Filters mapToFilters(List<Characteristic> characteristics) {
        Filters filters = new Filters();
        List<TextFilter> textFilters = new ArrayList<>();
        List<NumericFilter> numericFilters = new ArrayList<>();

        for(var characteristic : characteristics) {
            if(characteristic.getIsText()) {
                textFilters.add(TextFilter.builder()
                        .id(characteristic.getId())
                        .name(characteristic.getName())
                        .values(characteristic.getTextValues().stream()
                                .map(TextValue::getTextValue)
                                .toList())
                        .build());
            } else {
                numericFilters.add(NumericFilter.builder()
                        .id(characteristic.getId())
                        .name(characteristic.getName())
                        .build());
            }
        }

        filters.setTextFilters(textFilters);
        filters.setNumericFilters(numericFilters);
        return filters;
    }

    public ProductDocument mapToProductDocument(Product product) {
        ProductDocument productDocument = modelMapper.map(product, ProductDocument.class);
        productDocument.getCharacteristics().clear(); //todo: хз что тут происходит, но избавиться от этого
        List<ProductCharacteristicDocument> characteristicDocuments = new ArrayList<>();

        for(var characteristic : product.getCharacteristics()) {
            characteristicDocuments.add(ProductCharacteristicDocument.builder()
                    .id(characteristic.getCharacteristic().getId())
                    .numericValue(characteristic.getNumericValue())
                    .textValue(characteristic.getTextValue())
                    .build());
        }

        productDocument.setCharacteristics(characteristicDocuments);
        return productDocument;
    }
}
