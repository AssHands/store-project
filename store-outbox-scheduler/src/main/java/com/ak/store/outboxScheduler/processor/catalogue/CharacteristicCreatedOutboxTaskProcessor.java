package com.ak.store.outboxScheduler.processor.catalogue;

import com.ak.store.common.event.catalogue.CharacteristicCreatedEvent;
import com.ak.store.common.model.catalogue.snapshot.CharacteristicSnapshotPayload;
import com.ak.store.outboxScheduler.kafka.EventProducerKafka;
import com.ak.store.outboxScheduler.model.OutboxTask;
import com.ak.store.outboxScheduler.model.OutboxTaskType;
import com.ak.store.outboxScheduler.processor.OutboxTaskProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CharacteristicCreatedOutboxTaskProcessor implements OutboxTaskProcessor {
    private final EventProducerKafka eventProducerKafka;

    @Override
    public void process(OutboxTask task) {
        CharacteristicCreatedEvent characteristicCreatedEvent = new CharacteristicCreatedEvent(
                task.getId(), new Gson().fromJson(task.getPayload(), CharacteristicSnapshotPayload.class));

        String characteristicId = characteristicCreatedEvent.getPayload().getCharacteristic().getId().toString();
        eventProducerKafka.send(characteristicCreatedEvent, characteristicId);
    }

    @Override
    public OutboxTaskType getType() {
        return OutboxTaskType.CHARACTERISTIC_CREATED;
    }
}
