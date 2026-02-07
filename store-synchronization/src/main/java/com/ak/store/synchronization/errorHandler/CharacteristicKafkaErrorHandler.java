package com.ak.store.synchronization.errorHandler;

import com.ak.store.kafka.storekafkastarter.DltEventProducerKafka;
import com.ak.store.kafka.storekafkastarter.KafkaEvent;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.characteristic.CharacteristicCreatedEvent;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.characteristic.CharacteristicDeletedEvent;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.characteristic.CharacteristicUpdatedEvent;
import com.ak.store.synchronization.util.KafkaTopicRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CharacteristicKafkaErrorHandler {
    private final DltEventProducerKafka dltEventProducerKafka;
    private final KafkaTopicRegistry topicRegistry;

    public void handleCreateError(CharacteristicCreatedEvent event, Exception e) {
        String key = event.getPayload().getCharacteristic().getId().toString();
        dltEventProducerKafka.sendAsync(event, getTopicByEvent(event), key);
    }

    public void handleUpdateError(CharacteristicUpdatedEvent event, Exception e) {
        String key = event.getPayload().getCharacteristic().getId().toString();
        dltEventProducerKafka.sendAsync(event, getTopicByEvent(event), key);
    }

    public void handleDeleteError(CharacteristicDeletedEvent event, Exception e) {
        String key = event.getCharacteristicId().toString();
        dltEventProducerKafka.sendAsync(event, getTopicByEvent(event), key);
    }

    private String getTopicByEvent(KafkaEvent event) {
        String topic = topicRegistry.getTopicByEvent(event.getClass());

        if (topic == null) {
            throw new IllegalArgumentException("No topic configured for event class: " + event.getClass().getName());
        }

        return topic;
    }
}
