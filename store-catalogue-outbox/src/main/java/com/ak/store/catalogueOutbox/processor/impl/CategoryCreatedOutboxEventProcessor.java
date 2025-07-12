package com.ak.store.catalogueOutbox.processor.impl;

import com.ak.store.common.kafka.catalogue.CategoryCreatedEvent;
import com.ak.store.common.snapshot.catalogue.CategorySnapshotPayload;
import com.ak.store.catalogueOutbox.kafka.EventProducerKafka;
import com.ak.store.catalogueOutbox.model.OutboxEvent;
import com.ak.store.catalogueOutbox.model.OutboxEventType;
import com.ak.store.catalogueOutbox.processor.OutboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryCreatedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var categoryCreatedEvent = new CategoryCreatedEvent(event.getId(),
                gson.fromJson(event.getPayload(), CategorySnapshotPayload.class));

        String categoryId = categoryCreatedEvent.getPayload().getCategory().getId().toString();
        eventProducerKafka.send(categoryCreatedEvent, getType(), categoryId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.CATEGORY_CREATED;
    }
}
