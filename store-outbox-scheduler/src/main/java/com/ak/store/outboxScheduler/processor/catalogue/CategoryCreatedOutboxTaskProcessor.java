package com.ak.store.outboxScheduler.processor.catalogue;

import com.ak.store.common.event.catalogue.CategoryCreatedEvent;
import com.ak.store.common.model.catalogue.snapshot.CategorySnapshotPayload;
import com.ak.store.outboxScheduler.kafka.EventProducerKafka;
import com.ak.store.outboxScheduler.model.OutboxTask;
import com.ak.store.outboxScheduler.model.OutboxTaskType;
import com.ak.store.outboxScheduler.processor.OutboxTaskProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryCreatedOutboxTaskProcessor implements OutboxTaskProcessor {
    private final EventProducerKafka eventProducerKafka;

    @Override
    public void process(OutboxTask task) {
        CategoryCreatedEvent categoryCreatedEvent = new CategoryCreatedEvent(
                task.getId(), new Gson().fromJson(task.getPayload(), CategorySnapshotPayload.class));

        String categoryId = categoryCreatedEvent.getPayload().getCategory().getId().toString();
        eventProducerKafka.send(categoryCreatedEvent, categoryId);
    }

    @Override
    public OutboxTaskType getType() {
        return OutboxTaskType.CATEGORY_CREATED;
    }
}
