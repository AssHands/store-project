package com.ak.store.catalogue.validator;

import com.ak.store.common.dto.catalogue.product.ProductCharacteristicDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class ProductCharacteristicValidator {
    public void validate(Iterable<ProductCharacteristicDTO> characteristicWriteDTOs,
                         Map<Long, List<String>> availableCharacteristics) {
        for(var characteristic : characteristicWriteDTOs) {
            List<String> textValues = availableCharacteristics.get(characteristic.getId());

            if(textValues == null) {
                throw new RuntimeException("characteristic with id=%s is not available"
                        .formatted(characteristic.getId()));
            }

            if(characteristic.getTextValue() != null) {
                if(characteristic.getNumericValue() != null) {
                    throw new RuntimeException("characteristic with id=%s has both text value and numeric value"
                            .formatted(characteristic.getId()));
                }

                if(textValues.isEmpty()) {
                    throw new RuntimeException("characteristic with id=%s is not a text one"
                            .formatted(characteristic.getId()));
                }

                if(!textValues.contains(characteristic.getTextValue())) {
                    throw new RuntimeException("not valid text value for characteristic with id=%s"
                            .formatted(characteristic.getId()));
                }
            }

            if(characteristic.getNumericValue() != null) {
                if(!textValues.isEmpty()) {
                    throw new RuntimeException("characteristic with id=%s is not a numeric one"
                            .formatted(characteristic.getId()));
                }
            }
        }
    }
}