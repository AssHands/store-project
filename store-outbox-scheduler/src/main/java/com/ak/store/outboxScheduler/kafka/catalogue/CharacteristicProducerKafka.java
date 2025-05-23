package com.ak.store.outboxScheduler.kafka.catalogue;

import com.ak.store.common.event.catalogue.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CharacteristicProducerKafka {
    @Qualifier("characteristicKafkaTemplate")
    private final KafkaTemplate<String, CharacteristicEvent> kafkaTemplate;

    @Value("${kafka.topics.characteristic-created}")
    private String CHARACTERISTIC_CREATED_TOPIC;

    @Value("${kafka.topics.characteristic-updated}")
    private String CHARACTERISTIC_UPDATED_TOPIC;

    @Value("${kafka.topics.characteristic-deleted}")
    private String CHARACTERISTIC_DELETED_TOPIC;

    public void send(CharacteristicCreatedEvent characteristicCreatedEvent) {
        try {
            SendResult<String, CharacteristicEvent> future = kafkaTemplate.send(CHARACTERISTIC_CREATED_TOPIC,
                    characteristicCreatedEvent.getPayload().getCharacteristic().getId().toString(),
                    characteristicCreatedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka" + CHARACTERISTIC_CREATED_TOPIC + "error");
        }
    }

    public void send(CharacteristicUpdatedEvent characteristicUpdatedEvent) {
        try {
            SendResult<String, CharacteristicEvent> future = kafkaTemplate.send(CHARACTERISTIC_UPDATED_TOPIC,
                    characteristicUpdatedEvent.getPayload().getCharacteristic().getId().toString(),
                    characteristicUpdatedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka" + CHARACTERISTIC_UPDATED_TOPIC + "error");
        }
    }

    public void send(CharacteristicDeletedEvent characteristicDeletedEvent) {
        try {
            SendResult<String, CharacteristicEvent> future = kafkaTemplate.send(CHARACTERISTIC_DELETED_TOPIC,
                    characteristicDeletedEvent.getPayload().getCharacteristic().getId().toString(),
                    characteristicDeletedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka" + CHARACTERISTIC_DELETED_TOPIC + "error");
        }
    }
}
