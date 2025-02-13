package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.RangeValue;
import com.ak.store.catalogue.model.entity.TextValue;
import com.ak.store.catalogue.repository.*;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.catalogue.validator.business.CharacteristicBusinessValidator;
import com.ak.store.common.model.catalogue.dto.CharacteristicDTO;
import com.ak.store.common.model.catalogue.dto.RangeValueDTO;
import com.ak.store.common.model.catalogue.dto.TextValueDTO;
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
    private final CatalogueMapper catalogueMapper;
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

    public Characteristic createOne(CharacteristicDTO characteristicDTO) {
        characteristicBusinessValidator.validateCreation(characteristicDTO);
        return characteristicRepo.save(catalogueMapper.mapToCharacteristic(characteristicDTO));
    }


    //todo: check if product use this characteristic before deleting
    public void deleteOne(Long id) {
        characteristicRepo.deleteById(id);
    }


    public Characteristic updateOne(Long id, CharacteristicDTO characteristicDTO) {
        Characteristic characteristic = findOne(id);
        characteristicBusinessValidator.validateUpdate(characteristicDTO);
        updateCharacteristic(characteristic, characteristicDTO);
        return characteristicRepo.save(characteristic);
    }

    public Characteristic createRangeValue(Long id, RangeValueDTO rangeValueDTO) {
        Characteristic characteristic = findOneWithRangeValues(id);
        characteristicBusinessValidator.validateCreationRangeValue(characteristic, rangeValueDTO);
        characteristic.getRangeValues().add(catalogueMapper.mapToRangeValue(rangeValueDTO, id));
        return characteristicRepo.save(characteristic);
    }

    public Characteristic createTextValue(Long id, TextValueDTO textValueDTO) {
        Characteristic characteristic = findOneWithTextValues(id);
        characteristicBusinessValidator.validateCreationTextValue(characteristic, textValueDTO);
        characteristic.getTextValues().add(catalogueMapper.mapToTextValue(textValueDTO, id));
        return characteristicRepo.save(characteristic);
    }

    public Characteristic deleteOneRangeValue(Long id, RangeValueDTO rangeValueDTO) {
        Characteristic characteristic = findOneWithRangeValues(id);
        int index = findRangeValueIndex(characteristic, rangeValueDTO);
        characteristic.getRangeValues().remove(index);
        return characteristicRepo.save(characteristic);
    }

    public Characteristic deleteOneTextValue(Long id, TextValueDTO textValueDTO) {
        Characteristic characteristic = findOneWithTextValues(id);
        int index = findTextValueIndex(characteristic, textValueDTO);
        characteristic.getTextValues().remove(index);
        return characteristicRepo.save(characteristic);
    }

    private void updateCharacteristic(Characteristic characteristic, CharacteristicDTO characteristicDTO) {
        if(characteristicDTO.getName() != null) {
            characteristic.setName(characteristicDTO.getName());
        }
    }

    private int findRangeValueIndex(Characteristic characteristic, RangeValueDTO rangeValueDTO) {
        int index = 0;
        for(RangeValue rangeValue : characteristic.getRangeValues()) {
            if(rangeValue.getFromValue().equals(rangeValueDTO.getFrom())
                    && rangeValue.getToValue().equals(rangeValueDTO.getTo())) {
                return index;
            }
            index++;
        }

        throw new RuntimeException("range value didn't find");
    }

    private int findTextValueIndex(Characteristic characteristic, TextValueDTO textValueDTO) {
        int index = 0;
        for(TextValue textValue : characteristic.getTextValues()) {
            if(textValue.getTextValue().equals(textValueDTO.getText())) {
                return index;
            }
            index++;
        }

        throw new RuntimeException("text value didn't find");
    }
}