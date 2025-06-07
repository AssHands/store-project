package com.ak.store.catalogueOutbox.processor.impl;

import com.ak.store.common.event.catalogue.ProductCreatedEvent;
import com.ak.store.common.snapshot.catalogue.ProductSnapshotPayload;
import com.ak.store.catalogueOutbox.kafka.EventProducerKafka;
import com.ak.store.catalogueOutbox.model.OutboxEvent;
import com.ak.store.catalogueOutbox.model.OutboxEventType;
import com.ak.store.catalogueOutbox.processor.OutboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductCreatedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var productCreatedEvent = new ProductCreatedEvent(event.getId(),
                gson.fromJson(event.getPayload(), ProductSnapshotPayload.class));

        String productId = productCreatedEvent.getPayload().getProduct().getId().toString();
        eventProducerKafka.send(productCreatedEvent, productId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.PRODUCT_CREATED;
    }
}
