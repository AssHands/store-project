package com.ak.store.synchronization.errorHandler;

import com.ak.store.common.kafka.catalogue.*;
import com.ak.store.synchronization.kafka.producer.DltProducerKafka;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CharacteristicKafkaErrorHandler {
    private final DltProducerKafka dltProducerKafka;

    public void handleCreateError(CharacteristicCreatedEvent event, Exception e) {
        String characteristicId = event.getPayload().getCharacteristic().getId().toString();
        dltProducerKafka.send(event, characteristicId);
    }

    public void handleUpdateError(CharacteristicUpdatedEvent event, Exception e) {
        String characteristicId = event.getPayload().getCharacteristic().getId().toString();
        dltProducerKafka.send(event, characteristicId);
    }

    public void handleDeleteError(CharacteristicDeletedEvent event, Exception e) {
        String characteristicId = event.getCharacteristicId().toString();
        dltProducerKafka.send(event, characteristicId);
    }
}
