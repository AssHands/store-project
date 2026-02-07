package com.ak.store.catalogue.service.outbox;

import com.ak.store.catalogue.mapper.CharacteristicMapper;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.TextValue;
import com.ak.store.catalogue.outbox.OutboxEventService;
import com.ak.store.catalogue.outbox.OutboxEventType;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.characteristic.CharacteristicPayloadSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CharacteristicOutboxService {
    private final CharacteristicRepo characteristicRepo;
    private final CharacteristicMapper characteristicMapper;
    private final OutboxEventService outboxEventService;

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveCreatedEvent(Long id) {
        var characteristic = findOne(id);

        var snapshot = CharacteristicPayloadSnapshot.builder()
                .characteristic(characteristicMapper.toSnapshot(characteristic))
                .numericValues(Collections.emptyList())
                .textValues(Collections.emptyList())
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.CHARACTERISTIC_CREATED);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveUpdatedEvent(Long id) {
        var characteristic = findOne(id);

        var snapshot = CharacteristicPayloadSnapshot.builder()
                .characteristic(characteristicMapper.toSnapshot(characteristic))
                .numericValues(characteristic.getNumericValues().stream()
                        .map(characteristicMapper::toNumericValueSnapshot)
                        .toList())
                .textValues(characteristic.getTextValues().stream()
                        .map(TextValue::getTextValue)
                        .toList())
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.CHARACTERISTIC_UPDATED);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveDeletedEvent(Long id) {
        var snapshot = id.toString();
        outboxEventService.createOne(snapshot, OutboxEventType.CHARACTERISTIC_DELETED);
    }

    private Characteristic findOne(Long id) {
        return characteristicRepo.findOneWithValuesById(id)
                .orElseThrow(() -> new RuntimeException("characteristic not found"));
    }
}
