package com.ak.store.outboxScheduler.processor.catalogue;

import com.ak.store.common.event.catalogue.CategoryCreatedEvent;
import com.ak.store.common.model.catalogue.snapshot.CategorySnapshotPayload;
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
public class CategoryCreatedOutboxTaskProcessor implements OutboxTaskProcessor {
    private final CategoryProducerKafka categoryProducerKafka;
    @Override
    public void process(List<OutboxTask> tasks) {
        for (OutboxTask task : tasks) {
            CategoryCreatedEvent categoryCreatedEvent = new CategoryCreatedEvent(
                    task.getId(), new Gson().fromJson(task.getPayload(), CategorySnapshotPayload.class)
            );

            categoryProducerKafka.send(categoryCreatedEvent);
        }
    }

    @Override
    public OutboxTaskType getType() {
        return OutboxTaskType.CATEGORY_CREATED;
    }
}
