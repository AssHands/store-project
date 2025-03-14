package com.ak.store.outboxScheduler.kafka.catalogue;

import com.ak.store.common.event.catalogue.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CharacteristicProducerKafka {
    @Qualifier("characteristicKafkaTemplate")
    private final KafkaTemplate<String, CharacteristicEvent> kafkaTemplate;

    public void send(CharacteristicCreatedEvent characteristicCreatedEvent) {
        try {
            SendResult<String, CharacteristicEvent> future = kafkaTemplate.send("characteristic-created-events",
                    characteristicCreatedEvent.getCharacteristic().getId().toString(), characteristicCreatedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka characteristic-created-events error");
        }
    }

    public void send(CharacteristicUpdatedEvent characteristicUpdatedEvent) {
        try {
            SendResult<String, CharacteristicEvent> future = kafkaTemplate.send("characteristic-updated-events",
                    characteristicUpdatedEvent.getCharacteristic().getId().toString(), characteristicUpdatedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka characteristic-updated-events error");
        }
    }

    public void send(CharacteristicDeletedEvent characteristicDeletedEvent) {
        try {
            SendResult<String, CharacteristicEvent> future = kafkaTemplate.send("characteristic-deleted-events",
                    characteristicDeletedEvent.getCharacteristic().getId().toString(), characteristicDeletedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka characteristic-deleted-events error");
        }
    }
}
