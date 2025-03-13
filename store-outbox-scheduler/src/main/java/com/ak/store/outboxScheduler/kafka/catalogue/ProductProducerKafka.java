package com.ak.store.outboxScheduler.kafka.catalogue;

import com.ak.store.common.event.catalogue.ProductCreatedEvent;
import com.ak.store.common.event.catalogue.ProductDeletedEvent;
import com.ak.store.common.event.catalogue.ProductEvent;
import com.ak.store.common.event.catalogue.ProductUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductProducerKafka {
    @Qualifier("productKafkaTemplate")
    private final KafkaTemplate<Long, ProductEvent> kafkaProductTemplate;

    public void send(ProductDeletedEvent productDeletedEvent) {
        try {
            SendResult<Long, ProductEvent> future = kafkaProductTemplate.send(
                    "product-deleted-events", productDeletedEvent.getProductId(), productDeletedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka product-deleted-events error");
        }
    }

    public void send(ProductCreatedEvent productCreatedEvent) {
        try {
            SendResult<Long, ProductEvent> future = kafkaProductTemplate.send(
                    "product-created-events", productCreatedEvent.getProduct().getId(), productCreatedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka product-created-events error");
        }
    }

    public void send(ProductUpdatedEvent productUpdatedEvent) {
        try {
            SendResult<Long, ProductEvent> future = kafkaProductTemplate.send(
                    "product-updated-events", productUpdatedEvent.getProduct().getId(), productUpdatedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka product-updated-events error");
        }
    }
}