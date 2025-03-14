package com.ak.store.catalogue.util.validator.business;

import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.common.model.catalogue.form.CharacteristicForm;
import com.ak.store.common.model.catalogue.form.RangeValueForm;
import com.ak.store.common.model.catalogue.form.TextValueForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CharacteristicBusinessValidator {
    private final CharacteristicRepo characteristicRepo;

    public void validateCreation(CharacteristicForm characteristicForm) {
        checkUniqName(characteristicForm.getName());
    }

    public void validateUpdate(CharacteristicForm characteristicForm) {
        checkUniqName(characteristicForm.getName());
    }

    public void validateCreationRangeValue(Characteristic characteristic, RangeValueForm rangeValueForm) {
        if(characteristic.getIsText()) {
            throw new RuntimeException("can't add range value to text characteristic");
        }

        for(var range : characteristic.getRangeValues()) {
            if(range.getFromValue().equals(rangeValueForm.getFrom())
                    && range.getToValue().equals(rangeValueForm.getTo())) {
                throw new RuntimeException("this range is already exist");
            }
        }
    }

    public void validateCreationTextValue(Characteristic characteristic, TextValueForm textValueForm) {
        if(!characteristic.getIsText()) {
            throw new RuntimeException("can't add text value to numeric characteristic");
        }

        for(var text : characteristic.getTextValues()) {
            if(text.getTextValue().equals(textValueForm.getText())) {
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
