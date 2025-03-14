package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.RangeValue;
import com.ak.store.catalogue.model.entity.TextValue;
import com.ak.store.catalogue.repository.*;
import com.ak.store.catalogue.util.mapper.CharacteristicMapper;
import com.ak.store.catalogue.util.validator.business.CharacteristicBusinessValidator;
import com.ak.store.common.model.catalogue.form.CharacteristicForm;
import com.ak.store.common.model.catalogue.form.RangeValueForm;
import com.ak.store.common.model.catalogue.form.TextValueForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


//    Session session = entityManager.unwrap(Session.class);
//    SessionFactory sessionFactory = session.getSessionFactory();
//    sessionFactory.getCache();
//    Session session = entityManager.unwrap(Session.class);
//    Statistics statistics = session.getSessionFactory().getStatistics();
//    CacheRegionStatistics cacheStatistics = statistics.getDomainDataRegionStatistics("static-data");
@Service
@RequiredArgsConstructor
public class CharacteristicService {
    private final CharacteristicMapper characteristicMapper;
    private final CharacteristicRepo characteristicRepo;
    private final CharacteristicBusinessValidator characteristicBusinessValidator;

    public List<Characteristic> findAllByCategoryId(Long categoryId) {
        return characteristicRepo.findAllWithTextValuesByCategoryId(categoryId);
    }

    public Characteristic findOne(Long id) {
        return characteristicRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("no characteristic found"));
    }

    public Characteristic findOneWithRangeValues(Long id) {
        return characteristicRepo.findOneWithRangeValuesById(id)
                .orElseThrow(() -> new RuntimeException("no characteristic found"));
    }

    public Characteristic findOneWithTextValues(Long id) {
        return characteristicRepo.findOneWithTextValuesById(id)
                .orElseThrow(() -> new RuntimeException("no characteristic found"));
    }

    public Characteristic createOne(CharacteristicForm characteristicForm) {
        characteristicBusinessValidator.validateCreation(characteristicForm);
        return characteristicRepo.save(characteristicMapper.toCharacteristic(characteristicForm));
    }

    //todo: check if product use this characteristic before deleting
    public Characteristic deleteOne(Long id) {
        var characteristic = findOneWithRangeValues(id);
        characteristicRepo.deleteById(id);
        return characteristic;
    }

    public Characteristic updateOne(Long id, CharacteristicForm characteristicForm) {
        Characteristic characteristic = findOne(id);
        characteristicBusinessValidator.validateUpdate(characteristicForm);
        updateCharacteristic(characteristic, characteristicForm);
        return characteristicRepo.save(characteristic);
    }

    public Characteristic createRangeValue(Long id, RangeValueForm rangeValueForm) {
        Characteristic characteristic = findOneWithRangeValues(id);
        characteristicBusinessValidator.validateCreationRangeValue(characteristic, rangeValueForm);
        characteristic.getRangeValues().add(characteristicMapper.toRangeValue(rangeValueForm, characteristic));
        return characteristicRepo.save(characteristic);
    }

    public Characteristic createTextValue(Long id, TextValueForm textValueForm) {
        Characteristic characteristic = findOneWithTextValues(id);
        characteristicBusinessValidator.validateCreationTextValue(characteristic, textValueForm);
        characteristic.getTextValues().add(characteristicMapper.toTextValue(textValueForm, characteristic));
        return characteristicRepo.save(characteristic);
    }

    public Characteristic deleteOneRangeValue(Long id, RangeValueForm rangeValueForm) {
        Characteristic characteristic = findOneWithRangeValues(id);
        int index = findRangeValueIndex(characteristic, rangeValueForm);
        characteristic.getRangeValues().remove(index);
        return characteristicRepo.save(characteristic);
    }

    public Characteristic deleteOneTextValue(Long id, TextValueForm textValueForm) {
        Characteristic characteristic = findOneWithTextValues(id);
        int index = findTextValueIndex(characteristic, textValueForm);
        characteristic.getTextValues().remove(index);
        return characteristicRepo.save(characteristic);
    }

    private void updateCharacteristic(Characteristic characteristic, CharacteristicForm characteristicForm) {
        if(characteristicForm.getName() != null) {
            characteristic.setName(characteristicForm.getName());
        }
    }

    private int findRangeValueIndex(Characteristic characteristic, RangeValueForm rangeValueForm) {
        int index = 0;
        for(RangeValue rangeValue : characteristic.getRangeValues()) {
            if(rangeValue.getFromValue().equals(rangeValueForm.getFrom())
                    && rangeValue.getToValue().equals(rangeValueForm.getTo())) {
                return index;
            }
            index++;
        }

        throw new RuntimeException("range value didn't find");
    }

    private int findTextValueIndex(Characteristic characteristic, TextValueForm textValueForm) {
        int index = 0;
        for(TextValue textValue : characteristic.getTextValues()) {
            if(textValue.getTextValue().equals(textValueForm.getText())) {
                return index;
            }
            index++;
        }

        throw new RuntimeException("text value didn't find");
    }
}