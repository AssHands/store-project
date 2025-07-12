package com.ak.store.catalogueOutbox.processor.impl;

import com.ak.store.common.kafka.catalogue.ProductUpdatedEvent;
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
public class ProductUpdatedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var productUpdatedEvent = new ProductUpdatedEvent(event.getId(),
                gson.fromJson(event.getPayload(), ProductSnapshotPayload.class));

        String productId = productUpdatedEvent.getPayload().getProduct().getId().toString();
        eventProducerKafka.send(productUpdatedEvent, getType(), productId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.PRODUCT_UPDATED;
    }
}
