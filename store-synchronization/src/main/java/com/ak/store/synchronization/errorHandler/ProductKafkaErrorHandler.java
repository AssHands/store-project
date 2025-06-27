package com.ak.store.synchronization.errorHandler;

import com.ak.store.common.kafka.catalogue.ProductCreatedEvent;
import com.ak.store.common.kafka.catalogue.ProductDeletedEvent;
import com.ak.store.common.kafka.catalogue.ProductRatingUpdatedEvent;
import com.ak.store.common.kafka.catalogue.ProductUpdatedEvent;
import com.ak.store.synchronization.kafka.producer.DltProducerKafka;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductKafkaErrorHandler {
    private final DltProducerKafka dltProducerKafka;

    public void handleCreateError(ProductCreatedEvent event, Exception e) {
        String productId = event.getPayload().getProduct().getId().toString();
        dltProducerKafka.send(event, productId);
    }

    public void handleUpdateError(ProductUpdatedEvent event, Exception e) {
        String productId = event.getPayload().getProduct().getId().toString();
        dltProducerKafka.send(event, productId);
    }

    public void handleDeleteError(ProductDeletedEvent event, Exception e) {
        String productId = event.getProductId().toString();
        dltProducerKafka.send(event, productId);
    }

    public void handleRatingUpdateError(ProductRatingUpdatedEvent event, Exception e) {
        String productId = event.getProductRating().getId().toString();
        dltProducerKafka.send(event, productId);
    }
}