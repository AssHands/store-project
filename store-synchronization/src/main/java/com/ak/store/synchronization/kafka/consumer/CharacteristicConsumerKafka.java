package com.ak.store.synchronization.kafka.consumer;

import com.ak.store.common.event.catalogue.CharacteristicCreatedEvent;
import com.ak.store.common.event.catalogue.CharacteristicDeletedEvent;
import com.ak.store.common.event.catalogue.CharacteristicUpdatedEvent;
import com.ak.store.synchronization.errorHandler.CharacteristicKafkaErrorHandler;
import com.ak.store.synchronization.facade.CharacteristicFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CharacteristicConsumerKafka {
    private final CharacteristicFacade characteristicFacade;
    private final CharacteristicKafkaErrorHandler errorHandler;

    @KafkaListener(
            topics = "${kafka.topics.characteristic-created}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleCreated(List<CharacteristicCreatedEvent> characteristicCreatedEvents) {
        for (var event : characteristicCreatedEvents) {
            try {
                characteristicFacade.createOne(event.getPayload());
            } catch (Exception e) {
                errorHandler.handleCreateError(event, e);
            }
        }
    }

    @KafkaListener(
            topics = "${kafka.topics.characteristic-updated}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleUpdated(List<CharacteristicUpdatedEvent> characteristicUpdatedEvents) {
        for (var event : characteristicUpdatedEvents) {
            try {
                characteristicFacade.updateOne(event.getPayload());
            } catch (Exception e) {
                errorHandler.handleUpdateError(event, e);
            }
        }
    }

    @KafkaListener(
            topics = "${kafka.topics.characteristic-deleted}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleDeleted(List<CharacteristicDeletedEvent> characteristicDeletedEvents) {
        for (var event : characteristicDeletedEvents) {
            try {
                characteristicFacade.deleteOne(event.getPayload().getCharacteristic().getId());
            } catch (Exception e) {
                errorHandler.handleDeleteError(event, e);
            }
        }
    }
}