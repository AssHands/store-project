package com.ak.store.catalogueOutbox.processor.impl;

import com.ak.store.catalogueOutbox.kafka.EventProducerKafka;
import com.ak.store.catalogueOutbox.model.OutboxEvent;
import com.ak.store.catalogueOutbox.model.OutboxEventType;
import com.ak.store.catalogueOutbox.processor.OutboxEventProcessor;
import com.ak.store.common.kafka.catalogue.ProductRatingUpdatedEvent;
import com.ak.store.common.snapshot.catalogue.ProductRatingUpdatedSnapshot;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductRatingUpdatedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;

    @Override
    public void process(OutboxEvent event) {
        var productRatingUpdatedEvent = new ProductRatingUpdatedEvent(event.getId(),
                gson.fromJson(event.getPayload(), ProductRatingUpdatedSnapshot.class));

        String productId = productRatingUpdatedEvent.getProductRating().getId().toString();
        eventProducerKafka.send(productRatingUpdatedEvent, productId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.PRODUCT_RATING_UPDATED;
    }
}