package com.ak.store.catalogue.utils;

import com.ak.store.catalogue.model.entity.ne.Characteristic;
import com.ak.store.common.dto.catalogue.product.CharacteristicWriteDTO;
import org.springframework.stereotype.Component;


@Component
public class CatalogueValidator {
    public boolean validateCharacteristic(Characteristic availableCharacteristic,
                                          CharacteristicWriteDTO characteristicDTO) {
        if(characteristicDTO.getTextValue() != null) {
            if(characteristicDTO.getNumericValue() != null) {
                throw new RuntimeException("Characteristic with id=%s has both text value and numeric value"
                        .formatted(characteristicDTO.getCharacteristicId()));
            }

            if(!availableCharacteristic.isText()) {
                throw new RuntimeException("Characteristic with id=%s is not a text one"
                        .formatted(characteristicDTO.getCharacteristicId()));
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
                        .formatted(characteristicDTO.getCharacteristicId()));
            }
        }

        if(characteristicDTO.getNumericValue() != null) {
            if(availableCharacteristic.isText()) {
                throw new RuntimeException("Characteristic with id=%s is not a numeric one"
                        .formatted(characteristicDTO.getCharacteristicId()));
            }
        }

        return true;
    }
}