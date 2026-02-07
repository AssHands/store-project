package com.ak.store.catalogue.service;

import com.ak.store.catalogue.mapper.CharacteristicMapper;
import com.ak.store.catalogue.model.command.WriteCharacteristicCommand;
import com.ak.store.catalogue.model.command.WriteNumericValueCommand;
import com.ak.store.catalogue.model.command.WriteTextValueCommand;
import com.ak.store.catalogue.model.dto.CharacteristicDTO;
import com.ak.store.catalogue.model.dto.NumericValueDTO;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.TextValue;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.catalogue.validator.CharacteristicValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacteristicService {
    private final CharacteristicMapper characteristicMapper;
    private final CharacteristicRepo characteristicRepo;
    private final CharacteristicValidator characteristicValidator;

    private Characteristic findOneById(Long id) {
        return characteristicRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    private Characteristic findOneWithValuesById(Long id) {
        return characteristicRepo.findOneWithValuesById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public List<CharacteristicDTO> findAllByCategoryId(Long categoryId) {
        return characteristicRepo.findAllByCategoryId(categoryId).stream()
                .map(characteristicMapper::toDTO)
                .toList();
    }

    public List<String> findAllTextValue(Long id) {
        return findOneWithValuesById(id).getTextValues().stream()
                .map(TextValue::getTextValue)
                .toList();
    }

    public List<NumericValueDTO> findAllNumericValue(Long id) {
        return findOneWithValuesById(id).getNumericValues().stream()
                .map(characteristicMapper::toNumericValueDTO)
                .toList();
    }

    @Transactional
    public CharacteristicDTO createOne(WriteCharacteristicCommand command) {
        characteristicValidator.validateCreate(command);

        var characteristic = characteristicMapper.toEntity(command);

        return characteristicMapper.toDTO(characteristicRepo.save(characteristic));
    }

    @Transactional
    public CharacteristicDTO updateOne(WriteCharacteristicCommand command) {
        characteristicValidator.validateUpdate(command);

        var characteristic = findOneById(command.getId());
        characteristicMapper.updateEntity(command, characteristic);

        return characteristicMapper.toDTO(characteristicRepo.save(characteristic));
    }

    //todo проверить, что продукты не используют эту характеристику перед удалением
    @Transactional
    public CharacteristicDTO deleteOne(Long id) {
        var characteristic = findOneById(id);

        characteristicRepo.delete(characteristic);

        return characteristicMapper.toDTO(characteristic);
    }

    @Transactional
    public CharacteristicDTO addOneNumericValue(WriteNumericValueCommand command) {
        characteristicValidator.validateAddOneNumericValue(command);

        var characteristic = findOneWithValuesById(command.getCharacteristicId());
        characteristic.getNumericValues().add(characteristicMapper.toNumericValue(command, characteristic.getId()));

        return characteristicMapper.toDTO(characteristicRepo.save(characteristic));
    }

    @Transactional
    public CharacteristicDTO removeOneNumericValue(WriteNumericValueCommand command) {
        var characteristic = findOneWithValuesById(command.getCharacteristicId());
        int index = findRangeValueIndex(characteristic, command);

        characteristic.getNumericValues().remove(index);
        return characteristicMapper.toDTO(characteristicRepo.save(characteristic));
    }

    @Transactional
    public CharacteristicDTO addOneTextValue(WriteTextValueCommand command) {
        characteristicValidator.validateAddOneTextValue(command);

        var characteristic = findOneWithValuesById(command.getCharacteristicId());
        characteristic.getTextValues().add(characteristicMapper.toTextValue(command, characteristic.getId()));

        return characteristicMapper.toDTO(characteristicRepo.save(characteristic));
    }

    @Transactional
    public CharacteristicDTO removeOneTextValue(WriteTextValueCommand command) {
        var characteristic = findOneWithValuesById(command.getCharacteristicId());
        int index = findTextValueIndex(characteristic, command);

        characteristic.getTextValues().remove(index);
        return characteristicMapper.toDTO(characteristicRepo.save(characteristic));
    }

    private int findRangeValueIndex(Characteristic characteristic, WriteNumericValueCommand command) {
        int index = 0;
        for (var nv : characteristic.getNumericValues()) {
            if (nv.getFromValue().equals(command.getFromValue())
                    && nv.getToValue().equals(command.getToValue())) {
                return index;
            }
            index++;
        }

        //todo перенести в слой валидации
        throw new RuntimeException("range value didn't find");
    }

    private int findTextValueIndex(Characteristic characteristic, WriteTextValueCommand command) {
        int index = 0;
        for (var tv : characteristic.getTextValues()) {
            if (tv.getTextValue().equals(command.getTextValue())) {
                return index;
            }
            index++;
        }

        //todo перенести в слой валидации
        throw new RuntimeException("text value didn't find");
    }
}