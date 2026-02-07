package com.ak.store.synchronization.kafka;

import com.ak.store.kafka.storekafkastarter.model.event.catalogue.characteristic.CharacteristicCreatedEvent;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.characteristic.CharacteristicDeletedEvent;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.characteristic.CharacteristicUpdatedEvent;
import com.ak.store.synchronization.errorHandler.CharacteristicKafkaErrorHandler;
import com.ak.store.synchronization.mapper.CharacteristicMapper;
import com.ak.store.synchronization.service.CharacteristicService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CharacteristicConsumerKafka {
    private final CharacteristicService characteristicService;
    private final CharacteristicMapper characteristicMapper;
    private final CharacteristicKafkaErrorHandler errorHandler;

    @KafkaListener(
            topics = "${kafka.topics.characteristic-created}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleCreated(List<CharacteristicCreatedEvent> characteristicCreatedEvents, Acknowledgment ack) {
        for (var event : characteristicCreatedEvents) {
            try {
                characteristicService.createOne(characteristicMapper.toWritePayloadCommand(event.getPayload()));
            } catch (Exception e) {
                errorHandler.handleCreateError(event, e);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "${kafka.topics.characteristic-updated}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleUpdated(List<CharacteristicUpdatedEvent> characteristicUpdatedEvents, Acknowledgment ack) {
        for (var event : characteristicUpdatedEvents) {
            try {
                characteristicService.updateOne(characteristicMapper.toWritePayloadCommand(event.getPayload()));
            } catch (Exception e) {
                errorHandler.handleUpdateError(event, e);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "${kafka.topics.characteristic-deleted}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleDeleted(List<CharacteristicDeletedEvent> characteristicDeletedEvents, Acknowledgment ack) {
        for (var event : characteristicDeletedEvents) {
            try {
                characteristicService.deleteOne(event.getCharacteristicId());
            } catch (Exception e) {
                errorHandler.handleDeleteError(event, e);
            }
        }

        ack.acknowledge();
    }
}