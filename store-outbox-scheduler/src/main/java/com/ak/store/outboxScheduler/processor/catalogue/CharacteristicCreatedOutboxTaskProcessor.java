package com.ak.store.outboxScheduler.processor.catalogue;

import com.ak.store.common.event.catalogue.CharacteristicCreatedEvent;
import com.ak.store.common.model.catalogue.snapshot.CharacteristicSnapshotPayload;
import com.ak.store.outboxScheduler.kafka.catalogue.CharacteristicProducerKafka;
import com.ak.store.outboxScheduler.model.OutboxTask;
import com.ak.store.outboxScheduler.model.OutboxTaskType;
import com.ak.store.outboxScheduler.processor.OutboxTaskProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacteristicCreatedOutboxTaskProcessor implements OutboxTaskProcessor {
    private final CharacteristicProducerKafka characteristicProducerKafka;

    @Override
    public void process(List<OutboxTask> tasks) {
        for (OutboxTask task : tasks) {
            CharacteristicCreatedEvent characteristicCreatedEvent = new CharacteristicCreatedEvent(
                    task.getId(), new Gson().fromJson(task.getPayload(), CharacteristicSnapshotPayload.class)
            );

            characteristicProducerKafka.send(characteristicCreatedEvent);
        }
    }

    @Override
    public OutboxTaskType getType() {
        return OutboxTaskType.CHARACTERISTIC_CREATED;
    }
}
