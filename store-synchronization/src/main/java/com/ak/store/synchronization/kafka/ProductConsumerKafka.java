package com.ak.store.synchronization.kafka;

import com.ak.store.common.event.catalogue.ProductCreatedEvent;
import com.ak.store.common.event.catalogue.ProductDeletedEvent;
import com.ak.store.common.event.catalogue.ProductUpdatedEvent;
import com.ak.store.synchronization.facade.ProductFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductConsumerKafka {
    private final ProductFacade productFacade;

    @KafkaListener(topics = "product-created-events", groupId = "synchronization-catalogue-group")
    public void handle(ProductCreatedEvent productCreatedEvent) {
        productFacade.createOneProduct(productCreatedEvent.getProduct());
    }

    @KafkaListener(topics = "product-updated-events", groupId = "synchronization-catalogue-group")
    public void handle(ProductUpdatedEvent productUpdatedEvent) {
        productFacade.updateOneProduct(productUpdatedEvent.getProduct());
    }

    @KafkaListener(topics = "product-deleted-events", groupId = "synchronization-catalogue-group")
    public void handle(ProductDeletedEvent productDeletedEvent) {
        productFacade.deleteOneProduct(productDeletedEvent.getProduct().getId());
    }
}