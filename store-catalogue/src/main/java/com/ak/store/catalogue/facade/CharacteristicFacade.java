package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.mapper.CharacteristicMapper;
import com.ak.store.catalogue.model.command.WriteCharacteristicCommand;
import com.ak.store.catalogue.model.command.WriteNumericValueCommand;
import com.ak.store.catalogue.model.command.WriteTextValueCommand;
import com.ak.store.catalogue.model.dto.CharacteristicDTO;
import com.ak.store.catalogue.outbox.OutboxEventService;
import com.ak.store.catalogue.outbox.OutboxEventType;
import com.ak.store.catalogue.service.CharacteristicService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacteristicFacade {
    private final CharacteristicService characteristicService;
    private final CharacteristicMapper characteristicMapper;
    private final OutboxEventService outboxEventService;

    public List<CharacteristicDTO> findAllByCategoryId(Long categoryId) {
        return characteristicService.findAllByCategoryId(categoryId);
    }

    @Transactional
    public Long createOne(WriteCharacteristicCommand command) {
        var characteristic = characteristicService.createOne(command);

//        var snapshot = CharacteristicSnapshotPayload.builder()
//                .characteristic(characteristicMapper.toSnapshot(characteristic))
//                .numericValues(Collections.emptyList())
//                .textValues(Collections.emptyList())
//                .build();
//
//        outboxEventService.createOne(snapshot, OutboxEventType.CHARACTERISTIC_CREATED);
        return characteristic.getId();
    }

    @Transactional
    public Long updateOne(WriteCharacteristicCommand command) {
        var characteristic = characteristicService.updateOne(command);
//        List<String> textValues = new ArrayList<>();
//        List<NumericValueDTO> numericValues = new ArrayList<>();
//
//        if(characteristic.getIsText()) {
//            textValues = characteristicService.findAllTextValue(characteristic.getId());
//        } else {
//            numericValues = characteristicService.findAllNumericValue(characteristic.getId());
//        }
//
//        var snapshot = CharacteristicSnapshotPayload.builder()
//                .characteristic(characteristicMapper.toSnapshot(characteristic))
//                .numericValues(characteristicMapper.toNumericValueSnapshot(numericValues))
//                .textValues(textValues)
//                .build();
//
//        outboxEventService.createOne(snapshot, OutboxEventType.CHARACTERISTIC_UPDATED);
        return characteristic.getId();
    }

    @Transactional
    public void deleteOne(Long id) {
        var characteristic = characteristicService.deleteOne(id);

        var snapshot = characteristic.getId().toString();

        outboxEventService.createOne(snapshot, OutboxEventType.CHARACTERISTIC_DELETED);
    }

    @Transactional
    public Long addOneNumericValue(WriteNumericValueCommand command) {
        var characteristic = characteristicService.addOneNumericValue(command);
//        List<NumericValueDTO> numericValues = characteristicService.findAllNumericValue(characteristic.getId());
//
//        var snapshot = CharacteristicSnapshotPayload.builder()
//                .characteristic(characteristicMapper.toSnapshot(characteristic))
//                .numericValues(characteristicMapper.toNumericValueSnapshot(numericValues))
//                .build();
//
//        outboxEventService.createOne(snapshot, OutboxEventType.CHARACTERISTIC_UPDATED);
        return characteristic.getId();
    }

    @Transactional
    public Long removeOneNumericValue(WriteNumericValueCommand command) {
        var characteristic = characteristicService.removeOneNumericValue(command);
//        List<NumericValueDTO> numericValues = characteristicService.findAllNumericValue(characteristic.getId());
//
//        var snapshot = CharacteristicSnapshotPayload.builder()
//                .characteristic(characteristicMapper.toSnapshot(characteristic))
//                .numericValues(characteristicMapper.toNumericValueSnapshot(numericValues))
//                .build();
//
//        outboxEventService.createOne(snapshot, OutboxEventType.CHARACTERISTIC_UPDATED);
        return characteristic.getId();
    }

    @Transactional
    public Long addOneTextValue(WriteTextValueCommand command) {
        var characteristic = characteristicService.addOneTextValue(command);
//        List<String> textValues = characteristicService.findAllTextValue(characteristic.getId());
//
//        var snapshot = CharacteristicSnapshotPayload.builder()
//                .characteristic(characteristicMapper.toSnapshot(characteristic))
//                .textValues(textValues)
//                .build();
//
//        outboxEventService.createOne(snapshot, OutboxEventType.CHARACTERISTIC_UPDATED);
        return characteristic.getId();
    }

    @Transactional
    public Long removeOneTextValue(WriteTextValueCommand command) {
        var characteristic = characteristicService.removeOneTextValue(command);
//        List<String> textValues = characteristicService.findAllTextValue(characteristic.getId());
//
//        var snapshot = CharacteristicSnapshotPayload.builder()
//                .characteristic(characteristicMapper.toSnapshot(characteristic))
//                .textValues(textValues)
//                .build();
//
//        outboxEventService.createOne(snapshot, OutboxEventType.CHARACTERISTIC_UPDATED);
        return characteristic.getId();
    }
}
