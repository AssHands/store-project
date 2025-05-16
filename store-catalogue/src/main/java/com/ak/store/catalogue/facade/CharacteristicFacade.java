package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.model.dto.CharacteristicDTO;
import com.ak.store.catalogue.model.dto.NumericValueDTO;
import com.ak.store.catalogue.model.dto.write.CharacteristicWriteDTO;
import com.ak.store.catalogue.model.dto.write.NumericValueWriteDTO;
import com.ak.store.catalogue.model.dto.write.TextValueWriteDTO;
import com.ak.store.catalogue.outbox.OutboxTaskService;
import com.ak.store.catalogue.outbox.OutboxTaskType;
import com.ak.store.catalogue.service.CharacteristicService;
import com.ak.store.catalogue.util.mapper.CharacteristicMapper;
import com.ak.store.common.model.catalogue.snapshot.CharacteristicSnapshotPayload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacteristicFacade {
    private final CharacteristicService characteristicService;
    private final CharacteristicMapper characteristicMapper;
    private final OutboxTaskService<CharacteristicSnapshotPayload> outboxTaskService;

    public List<CharacteristicDTO> findAllByCategoryId(Long categoryId) {
        return characteristicService.findAllByCategoryId(categoryId);
    }

    @Transactional
    public Long createOne(CharacteristicWriteDTO request) {
        var characteristic = characteristicService.createOne(request);

        var snapshot = CharacteristicSnapshotPayload.builder()
                .characteristic(characteristicMapper.toCharacteristicSnapshot(characteristic))
                .numericValues(Collections.emptyList())
                .textValues(Collections.emptyList())
                .build();

        outboxTaskService.createOneTask(snapshot, OutboxTaskType.CHARACTERISTIC_CREATED);
        return characteristic.getId();
    }

    @Transactional
    public Long updateOne(Long id, CharacteristicWriteDTO request) {
        var characteristic = characteristicService.updateOne(id, request);
        List<String> textValues = new ArrayList<>();
        List<NumericValueDTO> numericValues = new ArrayList<>();

        if(characteristic.getIsText()) {
            textValues = characteristicService.findAllTextValue(characteristic.getId());
        } else {
            numericValues = characteristicService.findAllNumericValue(characteristic.getId());
        }

        var snapshot = CharacteristicSnapshotPayload.builder()
                .characteristic(characteristicMapper.toCharacteristicSnapshot(characteristic))
                .numericValues(characteristicMapper.toNumericValueSnapshot(numericValues))
                .textValues(textValues)
                .build();

        outboxTaskService.createOneTask(snapshot, OutboxTaskType.CHARACTERISTIC_UPDATED);
        return characteristic.getId();
    }

    @Transactional
    public void deleteOne(Long id) {
        var characteristic = characteristicService.deleteOne(id);

        var snapshot = CharacteristicSnapshotPayload.builder()
                .characteristic(characteristicMapper.toCharacteristicSnapshot(characteristic))
                .numericValues(Collections.emptyList())
                .textValues(Collections.emptyList())
                .build();

        outboxTaskService.createOneTask(snapshot, OutboxTaskType.CHARACTERISTIC_DELETED);
    }

    @Transactional
    public Long addOneNumericValue(Long id, NumericValueWriteDTO request) {
        var characteristic = characteristicService.addOneNumericValue(id, request);
        List<String> textValues = new ArrayList<>();
        List<NumericValueDTO> numericValues = new ArrayList<>();

        if(characteristic.getIsText()) {
            textValues = characteristicService.findAllTextValue(characteristic.getId());
        } else {
            numericValues = characteristicService.findAllNumericValue(characteristic.getId());
        }

        var snapshot = CharacteristicSnapshotPayload.builder()
                .characteristic(characteristicMapper.toCharacteristicSnapshot(characteristic))
                .numericValues(characteristicMapper.toNumericValueSnapshot(numericValues))
                .textValues(textValues)
                .build();

        outboxTaskService.createOneTask(snapshot, OutboxTaskType.CHARACTERISTIC_UPDATED);
        return characteristic.getId();
    }

    @Transactional
    public Long removeOneNumericValue(Long id, NumericValueWriteDTO request) {
        var characteristic = characteristicService.removeOneNumericValue(id, request);
        List<String> textValues = new ArrayList<>();
        List<NumericValueDTO> numericValues = new ArrayList<>();

        if(characteristic.getIsText()) {
            textValues = characteristicService.findAllTextValue(characteristic.getId());
        } else {
            numericValues = characteristicService.findAllNumericValue(characteristic.getId());
        }

        var snapshot = CharacteristicSnapshotPayload.builder()
                .characteristic(characteristicMapper.toCharacteristicSnapshot(characteristic))
                .numericValues(characteristicMapper.toNumericValueSnapshot(numericValues))
                .textValues(textValues)
                .build();

        outboxTaskService.createOneTask(snapshot, OutboxTaskType.CHARACTERISTIC_UPDATED);
        return characteristic.getId();
    }

    @Transactional
    public Long addOneTextValue(Long id, TextValueWriteDTO request) {
        var characteristic = characteristicService.addOneTextValue(id, request);
        List<String> textValues = new ArrayList<>();
        List<NumericValueDTO> numericValues = new ArrayList<>();

        if(characteristic.getIsText()) {
            textValues = characteristicService.findAllTextValue(characteristic.getId());
        } else {
            numericValues = characteristicService.findAllNumericValue(characteristic.getId());
        }

        var snapshot = CharacteristicSnapshotPayload.builder()
                .characteristic(characteristicMapper.toCharacteristicSnapshot(characteristic))
                .numericValues(characteristicMapper.toNumericValueSnapshot(numericValues))
                .textValues(textValues)
                .build();

        outboxTaskService.createOneTask(snapshot, OutboxTaskType.CHARACTERISTIC_UPDATED);
        return characteristic.getId();
    }

    @Transactional
    public Long removeOneTextValue(Long id, TextValueWriteDTO request) {
        var characteristic = characteristicService.removeOneTextValue(id, request);
        List<String> textValues = new ArrayList<>();
        List<NumericValueDTO> numericValues = new ArrayList<>();

        if(characteristic.getIsText()) {
            textValues = characteristicService.findAllTextValue(characteristic.getId());
        } else {
            numericValues = characteristicService.findAllNumericValue(characteristic.getId());
        }

        var snapshot = CharacteristicSnapshotPayload.builder()
                .characteristic(characteristicMapper.toCharacteristicSnapshot(characteristic))
                .numericValues(characteristicMapper.toNumericValueSnapshot(numericValues))
                .textValues(textValues)
                .build();

        outboxTaskService.createOneTask(snapshot, OutboxTaskType.CHARACTERISTIC_UPDATED);
        return characteristic.getId();
    }
}
