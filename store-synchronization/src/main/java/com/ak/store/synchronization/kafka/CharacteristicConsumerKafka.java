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
            topics = "characteristic-created-events",
            groupId = "synchronization-catalogue-group",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleCreated(List<CharacteristicCreatedEvent> characteristicCreatedEvents) {
        characteristicFacade.createAll(characteristicCreatedEvents.stream()
                .map(CharacteristicCreatedEvent::getPayload)
                .toList());
    }

    @KafkaListener(
            topics = "characteristic-updated-events",
            groupId = "synchronization-catalogue-group",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleUpdated(List<CharacteristicUpdatedEvent> characteristicUpdatedEvents) {
        characteristicFacade.updateAll(characteristicUpdatedEvents.stream()
                .map(CharacteristicUpdatedEvent::getPayload)
                .toList());
    }

    @KafkaListener(
            topics = "characteristic-deleted-events",
            groupId = "synchronization-catalogue-group",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleDeleted(List<CharacteristicDeletedEvent> characteristicDeletedEvents) {
        characteristicFacade.deleteAll(characteristicDeletedEvents.stream()
                .map(CharacteristicDeletedEvent::getPayload)
                .map(CharacteristicDTOold::getId)
                .toList());
    }
}
