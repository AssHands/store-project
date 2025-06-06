package com.ak.store.catalogueOutbox.processor.impl;

import com.ak.store.common.event.catalogue.ProductDeletedEvent;
import com.ak.store.common.model.catalogue.snapshot.ProductSnapshotPayload;
import com.ak.store.catalogueOutbox.kafka.EventProducerKafka;
import com.ak.store.catalogueOutbox.model.OutboxEvent;
import com.ak.store.catalogueOutbox.model.OutboxEventType;
import com.ak.store.catalogueOutbox.processor.OutboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductDeletedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var productDeletedEvent = new ProductDeletedEvent(event.getId(),
                gson.fromJson(event.getPayload(), Long.class));

        String productId = productDeletedEvent.getProductId().toString();
        eventProducerKafka.send(productDeletedEvent, productId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.PRODUCT_DELETED;
    }
}
