package com.ak.store.catalogueOutbox.processor.catalogue;

import com.ak.store.common.event.catalogue.CharacteristicCreatedEvent;
import com.ak.store.common.model.catalogue.snapshot.CharacteristicSnapshotPayload;
import com.ak.store.catalogueOutbox.kafka.EventProducerKafka;
import com.ak.store.catalogueOutbox.model.OutboxEvent;
import com.ak.store.catalogueOutbox.model.OutboxEventType;
import com.ak.store.catalogueOutbox.processor.OutboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CharacteristicCreatedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;

    @Override
    public void process(OutboxEvent task) {
        CharacteristicCreatedEvent characteristicCreatedEvent = new CharacteristicCreatedEvent(
                task.getId(), new Gson().fromJson(task.getPayload(), CharacteristicSnapshotPayload.class));

        String characteristicId = characteristicCreatedEvent.getPayload().getCharacteristic().getId().toString();
        eventProducerKafka.send(characteristicCreatedEvent, characteristicId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.CHARACTERISTIC_CREATED;
    }
}
