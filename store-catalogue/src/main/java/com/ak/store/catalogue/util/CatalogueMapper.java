package com.ak.store.catalogue.util;

import com.ak.store.catalogue.model.entity.*;
import com.ak.store.common.document.CharacteristicDocument;
import com.ak.store.common.document.ImageDocument;
import com.ak.store.common.document.ProductDocument;
import com.ak.store.common.model.catalogue.dto.*;
import com.ak.store.common.model.catalogue.view.*;
import com.ak.store.common.model.search.dto.FiltersDTO;
import com.ak.store.common.model.search.common.NumericFilter;
import com.ak.store.common.model.search.common.TextFilter;
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

    public Product mapToProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        product.setId(null); //todo: маппер присваивает id категории к продукту

        if (productDTO.getDiscountPercentage() == null || productDTO.getDiscountPercentage() == 0) {
            product.setDiscountPercentage(0);
            product.setCurrentPrice(product.getFullPrice());
        } else {
            int discount = product.getFullPrice() * product.getDiscountPercentage() / 100;
            int priceWithDiscount = product.getFullPrice() - discount;
            product.setCurrentPrice(priceWithDiscount);
        }

        if (productDTO.getCategoryId() != null) {
            product.setCategory(
                    Category.builder()
                            .id(productDTO.getCategoryId())
                            .build());
        }

        return product;
    }

    public ProductPoorView mapToProductPoorView(Product product) {
        return modelMapper.map(product, ProductPoorView.class);
    }

    public ProductPoorView mapToProductPoorView(ProductDocument product) {
        return modelMapper.map(product, ProductPoorView.class);
    }

    public ProductRichView mapToProductRichView(Product product) {
        var view = modelMapper.map(product, ProductRichView.class);
        view.getCharacteristics().clear();

        for (var src : product.getCharacteristics()) {
            view.getCharacteristics().add(ProductCharacteristicView.builder()
                    .numericValue(src.getNumericValue())
                    .textValue(src.getTextValue())
                    .name(src.getCharacteristic().getName())
                    .id(src.getCharacteristic().getId())
                    .build());
        }

        return view;
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

    public CategoryView mapToCategoryView(Category category) {
        return modelMapper.map(category, CategoryView.class);
    }

    public FiltersDTO mapToFiltersDTO(List<Characteristic> characteristics) {
        FiltersDTO filtersDTO = new FiltersDTO();
        List<TextFilter> textFilters = new ArrayList<>();
        List<NumericFilter> numericFilters = new ArrayList<>();

        for (var characteristic : characteristics) {
            if (characteristic.getIsText()) {
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

        filtersDTO.setTextFilters(textFilters);
        filtersDTO.setNumericFilters(numericFilters);
        return filtersDTO;
    }

    public ProductDocument mapToProductDocument(Product product) {
        ProductDocument productDocument = modelMapper.map(product, ProductDocument.class);

        productDocument.getCharacteristics().clear(); //todo: хз что тут происходит, но избавиться от этого
        productDocument.getImages().clear();

        List<CharacteristicDocument> characteristicDocuments = new ArrayList<>();
        for (var characteristic : product.getCharacteristics()) {
            characteristicDocuments.add(CharacteristicDocument.builder()
                    .id(characteristic.getCharacteristic().getId())
                    .numericValue(characteristic.getNumericValue())
                    .textValue(characteristic.getTextValue())
                    .build());
        }

        List<ImageDocument> imageDocumentList = new ArrayList<>();
        for (var image : product.getImages()) {
            imageDocumentList.add(
                    ImageDocument.builder()
                            .imageKey(image.getImageKey())
                            .index(image.getIndex())
                            .build()
            );
        }

        productDocument.setCharacteristics(characteristicDocuments);
        productDocument.setImages(imageDocumentList);

        System.out.println(productDocument);
        return productDocument;
    }

    public CharacteristicView mapToCharacteristicView(Characteristic characteristic) {
        return CharacteristicView.builder()
                .id(characteristic.getId())
                .name(characteristic.getName())
                .isText(characteristic.getIsText())
                .textValues(characteristic.getTextValues().stream()
                        .map(TextValue::getTextValue)
                        .toList())
                .build();
    }

    public Category mapToCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setId(null);
        category.setParentId(category.getParentId());
        return category;
    }

    public Characteristic mapToCharacteristic(CharacteristicDTO characteristicDTO) {
        return modelMapper.map(characteristicDTO, Characteristic.class);
    }

    public RangeValue mapToRangeValue(RangeValueDTO rangeValueDTO, Long characteristicId) {
        RangeValue rangeValue = modelMapper.map(rangeValueDTO, RangeValue.class);
        rangeValue.setCharacteristic(Characteristic.builder().id(characteristicId).build());
        return rangeValue;
    }

    public TextValue mapToTextValue(TextValueDTO textValueDTO, Long id) {
        TextValue textValue = modelMapper.map(textValueDTO, TextValue.class);
        textValue.setCharacteristic(Characteristic.builder().id(id).build());
        return textValue;
    }

    public ProductPrice mapToProductPrice(Product product) {
        return new ProductPrice(product.getId(), product.getCurrentPrice());
    }
}
