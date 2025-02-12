package com.ak.store.catalogue.validator;

import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.common.model.catalogue.dto.CharacteristicDTO;
import com.ak.store.common.model.catalogue.dto.RangeValueDTO;
import com.ak.store.common.model.catalogue.dto.TextValueDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CharacteristicBusinessValidator {
    private final CharacteristicRepo characteristicRepo;

    public void validateCreation(CharacteristicDTO characteristicDTO) {
        checkUniqName(characteristicDTO.getName());
    }

    public void validateUpdate(CharacteristicDTO characteristicDTO) {
        checkUniqName(characteristicDTO.getName());
    }

    public void validateCreationRangeValue(Characteristic characteristic, RangeValueDTO rangeValueDTO) {
        if(characteristic.getIsText()) {
            throw new RuntimeException("can't add range value to text characteristic");
        }

        for(var range : characteristic.getRangeValues()) {
            if(range.getFromValue().equals(rangeValueDTO.getFrom())
                    && range.getToValue().equals(rangeValueDTO.getTo())) {
                throw new RuntimeException("this range is already exist");
            }
        }
    }

    public void validateCreationTextValue(Characteristic characteristic, TextValueDTO textValueDTO) {
        if(!characteristic.getIsText()) {
            throw new RuntimeException("can't add text value to numeric characteristic");
        }

        for(var text : characteristic.getTextValues()) {
            if(text.getTextValue().equals(textValueDTO.getText())) {
                throw new RuntimeException("this text value is already exist");
            }
        }
    }

    private void checkUniqName(String name) {
        if(characteristicRepo.existsByNameEqualsIgnoreCase(name)) {
            throw new RuntimeException("not uniq name");
        }
    }
}
