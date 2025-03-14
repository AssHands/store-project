package com.ak.store.outboxScheduler.processor.catalogue;

import com.ak.store.common.event.catalogue.CharacteristicDeletedEvent;
import com.ak.store.common.model.catalogue.dto.CharacteristicDTO;
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
public class CharacteristicDeletedOutboxTaskProcessor implements OutboxTaskProcessor {
    private final CharacteristicProducerKafka characteristicProducerKafka;

    @Override
    public void process(List<OutboxTask> tasks) {
        for (OutboxTask task : tasks) {
            CharacteristicDeletedEvent characteristicDeletedEvent = new CharacteristicDeletedEvent(
                    task.getId(), new Gson().fromJson(task.getPayload(), CharacteristicDTO.class)
            );

            characteristicProducerKafka.send(characteristicDeletedEvent);
        }
    }

    @Override
    public OutboxTaskType getType() {
        return OutboxTaskType.CHARACTERISTIC_DELETED;
    }
}
