package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.outbox.OutboxTaskService;
import com.ak.store.catalogue.outbox.OutboxTaskType;
import com.ak.store.catalogue.service.CharacteristicService;
import com.ak.store.catalogue.util.mapper.CharacteristicMapper;
import com.ak.store.common.model.catalogue.dto.CharacteristicDTO;
import com.ak.store.common.model.catalogue.form.CharacteristicForm;
import com.ak.store.common.model.catalogue.form.RangeValueForm;
import com.ak.store.common.model.catalogue.form.TextValueForm;
import com.ak.store.common.model.catalogue.view.CharacteristicView;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacteristicFacade {
    private final CharacteristicService characteristicService;
    private final CharacteristicMapper characteristicMapper;
    private final OutboxTaskService<CharacteristicDTO> outboxTaskService;

    public List<CharacteristicView> findAllByCategoryId(Long categoryId) {
        return characteristicService.findAllByCategoryId(categoryId).stream()
                .map(characteristicMapper::toCharacteristicView)
                .toList();
    }

    @Transactional
    public Long createOne(CharacteristicForm characteristicForm) {
        var characteristic = characteristicService.createOne(characteristicForm);
        outboxTaskService.createOneTask(characteristicMapper.toCharacteristicDTO(characteristic),
                OutboxTaskType.CHARACTERISTIC_CREATED);
        return characteristic.getId();
    }

    @Transactional
    public void deleteOne(Long id) {
        var characteristic = characteristicService.deleteOne(id);
        outboxTaskService.createOneTask(characteristicMapper.toCharacteristicDTO(characteristic),
                OutboxTaskType.CHARACTERISTIC_DELETED);
    }

    @Transactional
    public Long updateOne(Long id, CharacteristicForm characteristicForm) {
        var characteristic = characteristicService.updateOne(id, characteristicForm);
        outboxTaskService.createOneTask(characteristicMapper.toCharacteristicDTO(characteristic),
                OutboxTaskType.CHARACTERISTIC_UPDATED);
        return characteristic.getId();
    }

    @Transactional
    public Long createOneRangeValue(Long id, RangeValueForm rangeValueForm) {
        var characteristic = characteristicService.createRangeValue(id, rangeValueForm);
        outboxTaskService.createOneTask(characteristicMapper.toCharacteristicDTO(characteristic),
                OutboxTaskType.CHARACTERISTIC_UPDATED);
        return characteristic.getId();
    }

    @Transactional
    public Long createOneTextValue(Long id, TextValueForm textValueForm) {
        var characteristic = characteristicService.createTextValue(id, textValueForm);
        outboxTaskService.createOneTask(characteristicMapper.toCharacteristicDTO(characteristic),
                OutboxTaskType.CHARACTERISTIC_UPDATED);
        return characteristic.getId();
    }

    @Transactional
    public Long deleteOneRangeValue(Long id, RangeValueForm rangeValueForm) {
        var characteristic = characteristicService.deleteOneRangeValue(id, rangeValueForm);
        outboxTaskService.createOneTask(characteristicMapper.toCharacteristicDTO(characteristic),
                OutboxTaskType.CHARACTERISTIC_UPDATED);
        return characteristic.getId();
    }

    @Transactional
    public Long deleteOneTextValue(Long id, TextValueForm textValueForm) {
        var characteristic = characteristicService.deleteOneTextValue(id, textValueForm);
        outboxTaskService.createOneTask(characteristicMapper.toCharacteristicDTO(characteristic),
                OutboxTaskType.CHARACTERISTIC_UPDATED);
        return characteristic.getId();
    }
}
