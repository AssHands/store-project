package com.ak.store.catalogueOutbox.processor.catalogue;

import com.ak.store.common.event.catalogue.CategoryUpdatedEvent;
import com.ak.store.common.model.catalogue.snapshot.CategorySnapshotPayload;
import com.ak.store.catalogueOutbox.kafka.EventProducerKafka;
import com.ak.store.catalogueOutbox.model.OutboxEvent;
import com.ak.store.catalogueOutbox.model.OutboxEventType;
import com.ak.store.catalogueOutbox.processor.OutboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryUpdatedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;

    @Override
    public void process(OutboxEvent task) {
        CategoryUpdatedEvent categoryUpdatedEvent = new CategoryUpdatedEvent(
                task.getId(), new Gson().fromJson(task.getPayload(), CategorySnapshotPayload.class));

        String categoryId = categoryUpdatedEvent.getPayload().getCategory().getId().toString();
        eventProducerKafka.send(categoryUpdatedEvent, categoryId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.CATEGORY_UPDATED;
    }
}
