package com.ak.store.catalogue.util.mapper;

import com.ak.store.catalogue.model.entity.*;
import com.ak.store.common.document.CharacteristicDocument;
import com.ak.store.common.document.ImageDocument;
import com.ak.store.common.document.ProductDocument;
import com.ak.store.common.model.catalogue.dto.ProductDTO;
import com.ak.store.common.model.catalogue.form.*;
import com.ak.store.common.model.catalogue.view.*;
import com.ak.store.common.model.search.dto.FiltersDTO;
import com.ak.store.common.model.search.common.NumericFilter;
import com.ak.store.common.model.search.common.TextFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CatalogueMapper0 {
    private final ModelMapper modelMapper;

    @Autowired
    public CatalogueMapper0(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Product mapToProduct(ProductForm productForm) {
        Product product = modelMapper.map(productForm, Product.class);
        product.setId(null); //todo: маппер присваивает id категории к продукту

        if (productForm.getDiscountPercentage() == null || productForm.getDiscountPercentage() == 0) {
            product.setDiscountPercentage(0);
            product.setCurrentPrice(product.getFullPrice());
        } else {
            int discount = product.getFullPrice() * product.getDiscountPercentage() / 100;
            int priceWithDiscount = product.getFullPrice() - discount;
            product.setCurrentPrice(priceWithDiscount);
        }

        if (productForm.getCategoryId() != null) {
            product.setCategory(
                    Category.builder()
                            .id(productForm.getCategoryId())
                            .build());
        }

        return product;
    }

    //method required product entity instead of product id cuz product doesn't have id when we just created it, for Hibernate
    public ProductCharacteristic mapToProductCharacteristic(ProductCharacteristicForm productCharacteristicForm, Product product) {
        return ProductCharacteristic.builder()
                .numericValue(productCharacteristicForm.getNumericValue())
                .textValue(productCharacteristicForm.getTextValue())
                .product(product)
                .characteristic(Characteristic.builder()
                        .id(productCharacteristicForm.getId())
                        .build())
                .build();
    }

    public Category mapToCategory(CategoryForm categoryForm) {
        Category category = modelMapper.map(categoryForm, Category.class);
        category.setId(null);
        category.setParentId(category.getParentId());
        return category;
    }

    public Characteristic mapToCharacteristic(CharacteristicForm characteristicForm) {
        return modelMapper.map(characteristicForm, Characteristic.class);
    }

    public RangeValue mapToRangeValue(RangeValueForm rangeValueForm, Long characteristicId) {
        RangeValue rangeValue = modelMapper.map(rangeValueForm, RangeValue.class);
        rangeValue.setCharacteristic(Characteristic.builder().id(characteristicId).build());
        return rangeValue;
    }

    public TextValue mapToTextValue(TextValueForm textValueForm, Long id) {
        TextValue textValue = modelMapper.map(textValueForm, TextValue.class);
        textValue.setCharacteristic(Characteristic.builder().id(id).build());
        return textValue;
    }
}
