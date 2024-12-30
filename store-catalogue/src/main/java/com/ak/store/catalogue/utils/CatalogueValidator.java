package com.ak.store.catalogue.utils;

import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.TextValue;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.common.dto.catalogue.product.ProductCharacteristicDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class CatalogueValidator {

    private final CharacteristicRepo characteristicRepo;

    public CatalogueValidator(CharacteristicRepo characteristicRepo) {
        this.characteristicRepo = characteristicRepo;
    }

    public boolean validateCharacteristic(Characteristic availableCharacteristic,
                                          ProductCharacteristicDTO characteristicDTO) {
        if(characteristicDTO.getTextValue() != null) {
            if(characteristicDTO.getNumericValue() != null) {
                throw new RuntimeException("Characteristic with id=%s has both text value and numeric value"
                        .formatted(characteristicDTO.getId()));
            }

            if(!availableCharacteristic.isText()) {
                throw new RuntimeException("Characteristic with id=%s is not a text one"
                        .formatted(characteristicDTO.getId()));
            }

            boolean isContain = false;
            for(var textValue : availableCharacteristic.getTextValues()) {
                if(textValue.getTextValue().equals(characteristicDTO.getTextValue())) {
                    isContain = true;
                    break;
                }
            }

            if(!isContain) {
                throw new RuntimeException("Not valid text value for characteristic with id=%s"
                        .formatted(characteristicDTO.getId()));
            }
        }

        if(characteristicDTO.getNumericValue() != null) {
            if(availableCharacteristic.isText()) {
                throw new RuntimeException("Characteristic with id=%s is not a numeric one"
                        .formatted(characteristicDTO.getId()));
            }
        }

        return true;
    }

    public void validateCharacteristics(Iterable<ProductCharacteristicDTO> characteristics, Long categoryId) {
        var availableMap = characteristicRepo.findTextValuesByCategoryId(categoryId).stream()
                .collect(Collectors.toMap(
                        Characteristic::getId,
                        characteristic -> characteristic.getTextValues().stream().map(TextValue::getTextValue).toList()));

        for(var characteristic : characteristics) {
            List<String> textValues = availableMap.get(characteristic.getId());

            if(textValues == null) {
                throw new RuntimeException("Characteristic with id=%s is not available"
                        .formatted(characteristic.getId()));
            }

            if(characteristic.getTextValue() != null) {
                if(characteristic.getNumericValue() != null) {
                    throw new RuntimeException("Characteristic with id=%s has both text value and numeric value"
                            .formatted(characteristic.getId()));
                }

                if(textValues.isEmpty()) {
                    throw new RuntimeException("Characteristic with id=%s is not a text one"
                            .formatted(characteristic.getId()));
                }

                if(!textValues.contains(characteristic.getTextValue())) {
                    throw new RuntimeException("Not valid text value for characteristic with id=%s"
                            .formatted(characteristic.getId()));
                }
            }

            if(characteristic.getNumericValue() != null) {
                if(!textValues.isEmpty()) {
                    throw new RuntimeException("Characteristic with id=%s is not a numeric one"
                            .formatted(characteristic.getId()));
                }
            }
        }
    }
}