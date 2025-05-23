package com.ak.store.catalogue.validator.service;

import com.ak.store.catalogue.model.dto.CharacteristicDTO;
import com.ak.store.catalogue.model.dto.NumericValueDTO;
import com.ak.store.catalogue.model.dto.write.CharacteristicWriteDTO;
import com.ak.store.catalogue.model.dto.write.NumericValueWriteDTO;
import com.ak.store.catalogue.model.dto.write.TextValueWriteDTO;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CharacteristicServiceValidator {
    private final CharacteristicRepo characteristicRepo;

    //todo добавить валидацию чтобы toValue было всегда больше или равно, чем fromValue

    public void validateCreating(CharacteristicWriteDTO characteristic) {
        checkUniqName(characteristic.getName());
    }

    public void validateUpdating(CharacteristicWriteDTO characteristic) {
        checkUniqName(characteristic.getName());
    }

    public void validateAddingNumericValue(CharacteristicDTO characteristic, List<NumericValueDTO> existingNumericValues,
                                           NumericValueWriteDTO addingNumericValue) {
        if (characteristic.getIsText()) {
            throw new RuntimeException("can't add range value to text characteristic");
        }

        for (var nv : existingNumericValues) {
            if (nv.getFromValue().equals(addingNumericValue.getFromValue())
                    && nv.getToValue().equals(addingNumericValue.getToValue())) {
                throw new RuntimeException("this range is already exist");
            }
        }
    }

    public void validateCreatingTextValue(CharacteristicDTO characteristic, List<String> existingTextValues,
                                          TextValueWriteDTO addingTextValue) {
        if (!characteristic.getIsText()) {
            throw new RuntimeException("can't add text value to numeric characteristic");
        }

        for (var tv : existingTextValues) {
            if (tv.equals(addingTextValue.getTextValue())) {
                throw new RuntimeException("this text value is already exist");
            }
        }
    }

    private void checkUniqName(String name) {
        if (characteristicRepo.existsByNameEqualsIgnoreCase(name)) {
            throw new RuntimeException("not uniq name");
        }
    }
}
