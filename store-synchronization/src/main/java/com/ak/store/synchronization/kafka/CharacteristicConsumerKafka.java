package com.ak.store.synchronization.kafka;

import com.ak.store.common.event.catalogue.*;
import com.ak.store.synchronization.facade.CharacteristicFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CharacteristicConsumerKafka {
    private final CharacteristicFacade characteristicFacade;

    @KafkaListener(
            topics = "${kafka.topics.characteristic-created}",
            groupId = "${kafka.group-id}",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleCreated(List<CharacteristicCreatedEvent> characteristicCreatedEvents) {
        characteristicFacade.createAll(characteristicCreatedEvents.stream()
                .map(CharacteristicCreatedEvent::getPayload)
                .toList());
    }

    @KafkaListener(
            topics = "${kafka.topics.characteristic-updated}",
            groupId = "${kafka.group-id}",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleUpdated(List<CharacteristicUpdatedEvent> characteristicUpdatedEvents) {
        characteristicFacade.updateAll(characteristicUpdatedEvents.stream()
                .map(CharacteristicUpdatedEvent::getPayload)
                .toList());
    }

    @KafkaListener(
            topics = "${kafka.topics.characteristic-deleted}",
            groupId = "${kafka.group-id}",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleDeleted(List<CharacteristicDeletedEvent> characteristicDeletedEvents) {
        characteristicFacade.deleteAll(characteristicDeletedEvents.stream()
                .map(v -> v.getPayload().getCharacteristic().getId())
                .toList());
    }
}
