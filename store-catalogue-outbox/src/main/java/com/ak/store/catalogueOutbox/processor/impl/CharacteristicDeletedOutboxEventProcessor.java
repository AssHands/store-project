package com.ak.store.catalogueOutbox.processor.impl;

import com.ak.store.common.event.catalogue.CharacteristicDeletedEvent;
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
public class CharacteristicDeletedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var characteristicDeletedEvent = new CharacteristicDeletedEvent(event.getId(),
                gson.fromJson(event.getPayload(), Long.class));

        String characteristicId = characteristicDeletedEvent.getCharacteristicId().toString();
        eventProducerKafka.send(characteristicDeletedEvent, characteristicId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.CHARACTERISTIC_DELETED;
    }
}
