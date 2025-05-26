package com.ak.store.catalogueOutbox.processor.catalogue;

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

    @Override
    public void process(OutboxEvent task) {
        CharacteristicDeletedEvent characteristicDeletedEvent = new CharacteristicDeletedEvent(
                task.getId(), new Gson().fromJson(task.getPayload(), CharacteristicSnapshotPayload.class));

        String characteristicId = characteristicDeletedEvent.getPayload().getCharacteristic().getId().toString();
        eventProducerKafka.send(characteristicDeletedEvent, characteristicId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.CHARACTERISTIC_DELETED;
    }
}
