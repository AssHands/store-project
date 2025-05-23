package com.ak.store.outboxScheduler.kafka.catalogue;

import com.ak.store.common.event.catalogue.ProductCreatedEvent;
import com.ak.store.common.event.catalogue.ProductDeletedEvent;
import com.ak.store.common.event.catalogue.ProductEvent;
import com.ak.store.common.event.catalogue.ProductUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductProducerKafka {
    @Qualifier("productKafkaTemplate")
    private final KafkaTemplate<String, ProductEvent> kafkaProductTemplate;

    @Value("${kafka.topics.product-created}")
    private String PRODUCT_CREATED_TOPIC;

    @Value("${kafka.topics.product-updated}")
    private String PRODUCT_UPDATED_TOPIC;

    @Value("${kafka.topics.product-deleted}")
    private String PRODUCT_DELETED_TOPIC;

    public void send(ProductCreatedEvent productCreatedEvent) {
        try {
            SendResult<String, ProductEvent> future = kafkaProductTemplate.send(PRODUCT_CREATED_TOPIC,
                    productCreatedEvent.getPayload().getProduct().getId().toString(), productCreatedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka" + PRODUCT_CREATED_TOPIC + "error");
        }
    }

    public void send(ProductUpdatedEvent productUpdatedEvent) {
        try {
            SendResult<String, ProductEvent> future = kafkaProductTemplate.send(PRODUCT_UPDATED_TOPIC,
                    productUpdatedEvent.getPayload().getProduct().getId().toString(), productUpdatedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka" + PRODUCT_UPDATED_TOPIC + "error");
        }
    }

    public void send(ProductDeletedEvent productDeletedEvent) {
        try {
            SendResult<String, ProductEvent> future = kafkaProductTemplate.send(PRODUCT_DELETED_TOPIC,
                    productDeletedEvent.getPayload().getProduct().getId().toString(), productDeletedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka" + PRODUCT_DELETED_TOPIC + "error");
        }
    }
}