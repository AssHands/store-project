package com.ak.store.catalogue.service;

import com.ak.store.catalogue.mapper.CharacteristicMapper;
import com.ak.store.catalogue.model.dto.CharacteristicDTO;
import com.ak.store.catalogue.model.dto.NumericValueDTO;
import com.ak.store.catalogue.model.dto.write.CharacteristicWriteDTO;
import com.ak.store.catalogue.model.dto.write.NumericValueWriteDTO;
import com.ak.store.catalogue.model.dto.write.TextValueWriteDTO;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.TextValue;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.catalogue.validator.service.CharacteristicServiceValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


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
    private final CharacteristicServiceValidator characteristicServiceValidator;

    private Characteristic findOneById(Long id) {
        return characteristicRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    private List<Characteristic> findAllByIds(List<Long> ids) {
        return characteristicRepo.findAllById(ids);
    }


    private Characteristic findOneByIdWithTextValues(Long id) {
        return characteristicRepo.findOneByIdWithTextValues(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    private Characteristic findOneByIdWithNumericValues(Long id) {
        return characteristicRepo.findOneByIdWithNumericValues(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public CharacteristicDTO findOne(Long id) {
        return characteristicMapper.toCharacteristicDTO(findOneById(id));
    }

    public List<CharacteristicDTO> findAll(List<Long> ids) {
        return characteristicMapper.toCharacteristicDTO(findAllByIds(ids));
    }

    public List<CharacteristicDTO> findAllByCategoryId(Long categoryId) {
        return characteristicMapper.toCharacteristicDTO(
                characteristicRepo.findAllByCategoryId(categoryId)
        );
    }

    public List<String> findAllTextValue(Long id) {
        return findOneByIdWithTextValues(id).getTextValues().stream()
                .map(TextValue::getTextValue)
                .toList();
    }

    public List<NumericValueDTO> findAllNumericValue(Long id) {
        return characteristicMapper.toNumericValueDTO(findOneByIdWithNumericValues(id).getNumericValues());
    }

    @Transactional
    public CharacteristicDTO createOne(CharacteristicWriteDTO request) {
        characteristicServiceValidator.validateCreating(request);
        var characteristic = characteristicMapper.toCharacteristic(request);
        return characteristicMapper.toCharacteristicDTO(characteristicRepo.save(characteristic));
    }

    @Transactional
    public CharacteristicDTO updateOne(Long id, CharacteristicWriteDTO request) {
        var characteristic = findOneById(id);
        characteristicServiceValidator.validateUpdating(request);

        updateOneFromDTO(characteristic, request);
        return characteristicMapper.toCharacteristicDTO(characteristicRepo.save(characteristic));
    }

    //todo: check if product use this characteristic before deleting
    @Transactional
    public CharacteristicDTO deleteOne(Long id) {
        var characteristic = findOneById(id);
        characteristicRepo.delete(characteristic);
        return characteristicMapper.toCharacteristicDTO(characteristic);
    }

    @Transactional
    public CharacteristicDTO addOneNumericValue(Long id, NumericValueWriteDTO request) {
        var characteristic = findOneByIdWithNumericValues(id);
        characteristicServiceValidator.validateAddingNumericValue(
                characteristicMapper.toCharacteristicDTO(characteristic), findAllNumericValue(id), request);

        characteristic.getNumericValues().add(characteristicMapper.toNumericValue(request, characteristic.getId()));
        return characteristicMapper.toCharacteristicDTO(characteristicRepo.save(characteristic));
    }

    @Transactional
    public CharacteristicDTO removeOneNumericValue(Long id, NumericValueWriteDTO request) {
        var characteristic = findOneByIdWithNumericValues(id);
        int index = findRangeValueIndex(characteristic, request);

        characteristic.getNumericValues().remove(index);
        return characteristicMapper.toCharacteristicDTO(characteristicRepo.save(characteristic));
    }

    @Transactional
    public CharacteristicDTO addOneTextValue(Long id, TextValueWriteDTO request) {
        var characteristic = findOneByIdWithTextValues(id);
        characteristicServiceValidator.validateCreatingTextValue(
                characteristicMapper.toCharacteristicDTO(characteristic), findAllTextValue(id), request);

        characteristic.getTextValues().add(characteristicMapper.toTextValue(request, characteristic.getId()));
        return characteristicMapper.toCharacteristicDTO(characteristicRepo.save(characteristic));
    }

    @Transactional
    public CharacteristicDTO removeOneTextValue(Long id, TextValueWriteDTO request) {
        var characteristic = findOneByIdWithTextValues(id);
        int index = findTextValueIndex(characteristic, request);

        characteristic.getTextValues().remove(index);
        return characteristicMapper.toCharacteristicDTO(characteristicRepo.save(characteristic));
    }

    private void updateOneFromDTO(Characteristic characteristic, CharacteristicWriteDTO request) {
        if (request.getName() != null) {
            characteristic.setName(request.getName());
        }
    }

    private int findRangeValueIndex(Characteristic characteristic, NumericValueWriteDTO request) {
        int index = 0;
        for (var nv : characteristic.getNumericValues()) {
            if (nv.getFromValue().equals(request.getFromValue())
                    && nv.getToValue().equals(request.getToValue())) {
                return index;
            }
            index++;
        }

        //todo перенести в слой валидации
        throw new RuntimeException("range value didn't find");
    }

    private int findTextValueIndex(Characteristic characteristic, TextValueWriteDTO request) {
        int index = 0;
        for (var tv : characteristic.getTextValues()) {
            if (tv.getTextValue().equals(request.getTextValue())) {
                return index;
            }
            index++;
        }

        //todo перенести в слой валидации
        throw new RuntimeException("text value didn't find");
    }
}