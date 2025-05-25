package com.ak.store.outboxScheduler.processor.catalogue;

import com.ak.store.common.event.catalogue.CategoryDeletedEvent;
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
public class CategoryDeletedOutboxTaskProcessor implements OutboxTaskProcessor {
    private final EventProducerKafka eventProducerKafka;

    @Override
    public void process(OutboxTask task) {
        CategoryDeletedEvent categoryDeletedEvent = new CategoryDeletedEvent(
                task.getId(), new Gson().fromJson(task.getPayload(), CategorySnapshotPayload.class));

        String categoryId = categoryDeletedEvent.getPayload().getCategory().getId().toString();
        eventProducerKafka.send(categoryDeletedEvent, categoryId);
    }

    @Override
    public OutboxTaskType getType() {
        return OutboxTaskType.CATEGORY_DELETED;
    }
}
