package com.ak.store.outboxScheduler.processor.catalogue;

import com.ak.store.common.event.catalogue.CharacteristicUpdatedEvent;
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
public class CharacteristicUpdatedOutboxTaskProcessor implements OutboxTaskProcessor {
    private final CharacteristicProducerKafka characteristicProducerKafka;

    @Override
    public void process(List<OutboxTask> tasks) {
        for (OutboxTask task : tasks) {
            CharacteristicUpdatedEvent characteristicUpdatedEvent = new CharacteristicUpdatedEvent(
                    task.getId(), new Gson().fromJson(task.getPayload(), CharacteristicDTOold.class)
            );

            characteristicProducerKafka.send(characteristicUpdatedEvent);
        }
    }

    @Override
    public OutboxTaskType getType() {
        return OutboxTaskType.CHARACTERISTIC_UPDATED;
    }
}
