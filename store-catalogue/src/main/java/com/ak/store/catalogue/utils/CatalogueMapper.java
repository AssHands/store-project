package com.ak.store.catalogue.utils;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.CharacteristicByCategory;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.common.dto.catalogue.others.AvailableCharacteristicDTO;
import com.ak.store.common.dto.catalogue.others.CategoryDTO;
import com.ak.store.common.dto.catalogue.product.ProductReadDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CatalogueMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public CatalogueMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProductReadDTO mapToProductSearchDTO(Product product) {
        return modelMapper.map(product, ProductReadDTO.class);
    }

    public CategoryDTO mapToCategoryDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }

    public List<AvailableCharacteristicDTO> mapToAvailableCharacteristicDTO(List<CharacteristicByCategory> characteristics) {
        Map<Long, AvailableCharacteristicDTO> uniqCharacteristics = new HashMap<>();

        for(var characteristic : characteristics) {
            List<String> textValues = new ArrayList<>();

            if(characteristic.getTextValue() != null) {
                for(var textCharacteristic : characteristics) {
                    if(textCharacteristic.getCharacteristicId().equals(characteristic.getCharacteristicId())) {
                        textValues.add(textCharacteristic.getTextValue());
                    }
                }
                uniqCharacteristics.put(characteristic.getCharacteristicId(),
                        new AvailableCharacteristicDTO(characteristic.getCharacteristicId(),
                                characteristic.getName(), textValues));

            } else {
                uniqCharacteristics.put(characteristic.getCharacteristicId(),
                        new AvailableCharacteristicDTO(characteristic.getCharacteristicId(), characteristic.getName()));
            }
        }

        return new ArrayList<>(uniqCharacteristics.values());
    }
}
