package com.ak.store.outboxScheduler.processor.catalogue;

import com.ak.store.common.event.catalogue.CategoryDeletedEvent;
import com.ak.store.outboxScheduler.kafka.catalogue.CategoryProducerKafka;
import com.ak.store.outboxScheduler.model.OutboxTask;
import com.ak.store.outboxScheduler.model.OutboxTaskType;
import com.ak.store.outboxScheduler.processor.OutboxTaskProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryDeletedOutboxTaskProcessor implements OutboxTaskProcessor {
    private final CategoryProducerKafka categoryProducerKafka;
    @Override
    public void process(List<OutboxTask> tasks) {
        for (OutboxTask task : tasks) {
            CategoryDeletedEvent categoryDeletedEvent = new CategoryDeletedEvent(
                    task.getId(), new Gson().fromJson(task.getPayload(), CategoryDTO.class)
            );

            categoryProducerKafka.send(categoryDeletedEvent);
        }
    }

    @Override
    public OutboxTaskType getType() {
        return OutboxTaskType.CATEGORY_DELETED;
    }
}
