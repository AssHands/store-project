package com.ak.store.outboxScheduler.processor.catalogue;

import com.ak.store.common.event.catalogue.CharacteristicUpdatedEvent;
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
public class CharacteristicUpdatedOutboxTaskProcessor implements OutboxTaskProcessor {
    private final EventProducerKafka eventProducerKafka;

    @Override
    public void process(OutboxTask task) {
        CharacteristicUpdatedEvent characteristicUpdatedEvent = new CharacteristicUpdatedEvent(
                task.getId(), new Gson().fromJson(task.getPayload(), CharacteristicSnapshotPayload.class));

        String characteristicId = characteristicUpdatedEvent.getPayload().getCharacteristic().getId().toString();
        eventProducerKafka.send(characteristicUpdatedEvent, characteristicId);
    }

    @Override
    public OutboxTaskType getType() {
        return OutboxTaskType.CHARACTERISTIC_UPDATED;
    }
}
