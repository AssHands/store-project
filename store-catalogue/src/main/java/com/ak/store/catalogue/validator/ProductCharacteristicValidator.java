package com.ak.store.catalogue.validator;

import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.TextValue;
import com.ak.store.common.dto.catalogue.ProductCharacteristicDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class ProductCharacteristicValidator {
    public void validate(Iterable<ProductCharacteristicDTO> ProductCharacteristicDTOs,
                         List<Characteristic> availableCharacteristics) {

        var availableTextValues = availableCharacteristics.stream()
                .collect(Collectors.toMap(
                        Characteristic::getId,
                        characteristic -> characteristic.getTextValues().stream().map(TextValue::getTextValue).toList())
                );

        for(var productCharacteristic : ProductCharacteristicDTOs) {
            List<String> textValues = availableTextValues.get(productCharacteristic.getId());

            if(textValues == null) {
                throw new RuntimeException("characteristic with id=%s is not available"
                        .formatted(productCharacteristic.getId()));
            }

            if(productCharacteristic.getTextValue() != null) {
                if(productCharacteristic.getNumericValue() != null) {
                    throw new RuntimeException("characteristic with id=%s has both text value and numeric value"
                            .formatted(productCharacteristic.getId()));
                }

                if(textValues.isEmpty()) {
                    throw new RuntimeException("characteristic with id=%s is not a text one"
                            .formatted(productCharacteristic.getId()));
                }

                if(!textValues.contains(productCharacteristic.getTextValue())) {
                    throw new RuntimeException("not valid text value for characteristic with id=%s"
                            .formatted(productCharacteristic.getId()));
                }
            }

            if(productCharacteristic.getNumericValue() != null) {
                if(!textValues.isEmpty()) {
                    throw new RuntimeException("characteristic with id=%s is not a numeric one"
                            .formatted(productCharacteristic.getId()));
                }
            }
        }
    }
}