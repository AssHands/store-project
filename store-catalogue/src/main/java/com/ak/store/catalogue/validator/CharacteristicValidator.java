package com.ak.store.catalogue.validator;

import com.ak.store.catalogue.model.command.WriteCharacteristicCommand;
import com.ak.store.catalogue.model.command.WriteNumericValueCommand;
import com.ak.store.catalogue.model.command.WriteTextValueCommand;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CharacteristicValidator {
    private final CharacteristicRepo characteristicRepo;

    //todo добавить валидацию чтобы toValue было всегда больше или равно, чем fromValue
    public void validateCreate(WriteCharacteristicCommand characteristic) {
        uniqName(characteristic.getName());
    }

    public void validateUpdate(WriteCharacteristicCommand characteristic) {
        uniqName(characteristic.getName());
    }

    public void validateAddOneNumericValue(WriteNumericValueCommand command) {
        characteristicIsNumeric(command.getCharacteristicId());
        numericValueNotExist(command.getCharacteristicId(), command.getFromValue(), command.getToValue());
    }

    public void validateAddOneTextValue(WriteTextValueCommand command) {
        characteristicIsText(command.getCharacteristicId());
        textValueNotExist(command.getCharacteristicId(), command.getTextValue());
    }

    private void uniqName(String name) {
        if (characteristicRepo.existsByNameEqualsIgnoreCase(name)) {
            throw new RuntimeException("not uniq name");
        }
    }

    private void characteristicIsNumeric(Long id) {
        var characteristic = findOneWithValuesById(id);

        if(characteristic.getIsText()) {
            throw new RuntimeException("characteristic must be numeric");
        }
    }

    private void characteristicIsText(Long id) {
        var characteristic = findOneWithValuesById(id);

        if(!characteristic.getIsText()) {
            throw new RuntimeException("characteristic must be text");
        }
    }

    private void textValueNotExist(Long id, String value) {
        var existingTextValues = findOneWithValuesById(id).getTextValues();

        //todo в map переделать
        for (var tv : existingTextValues) {
            if (tv.getTextValue().equals(value)) {
                throw new RuntimeException("this text value is already exist");
            }
        }
    }

    private void numericValueNotExist(Long id, Integer fromValue, Integer toValue) {
        var existingNumericValues = findOneWithValuesById(id).getNumericValues();

        //todo в map переделать
        for (var nv : existingNumericValues) {
            if (nv.getFromValue().equals(fromValue) && nv.getToValue().equals(toValue)) {
                throw new RuntimeException("this numeric value is already exist");
            }
        }
    }

    private Characteristic findOneWithValuesById(Long id) {
        return characteristicRepo.findOneWithValuesById(id)
                .orElseThrow(() -> new RuntimeException("characteristic not found"));
    }
}
