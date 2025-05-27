package com.ak.store.catalogueOutbox.processor.impl;

import com.ak.store.catalogueOutbox.kafka.EventProducerKafka;
import com.ak.store.catalogueOutbox.model.OutboxEvent;
import com.ak.store.catalogueOutbox.model.OutboxEventType;
import com.ak.store.catalogueOutbox.processor.OutboxEventProcessor;
import com.ak.store.common.event.catalogue.CategoryDeletedEvent;
import com.ak.store.common.model.catalogue.snapshot.CategorySnapshotPayload;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryDeletedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var categoryDeletedEvent = new CategoryDeletedEvent(event.getId(),
                gson.fromJson(event.getPayload(), CategorySnapshotPayload.class));

        String categoryId = categoryDeletedEvent.getPayload().getCategory().getId().toString();
        eventProducerKafka.send(categoryDeletedEvent, categoryId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.CATEGORY_DELETED;
    }
}
