package com.ak.store.synchronization.kafka;

import com.ak.store.common.event.catalogue.ProductCreatedEvent;
import com.ak.store.common.event.catalogue.ProductDeletedEvent;
import com.ak.store.common.event.catalogue.ProductUpdatedEvent;
import com.ak.store.synchronization.facade.ProductFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductConsumerKafka {
    private final ProductFacade productFacade;

    @KafkaListener(
            topics = "product-created-events",
            groupId = "synchronization-catalogue-group",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleCreated(List<ProductCreatedEvent> productCreatedEvents) {
        productFacade.createAll(productCreatedEvents.stream()
                .map(ProductCreatedEvent::getPayload)
                .toList());
    }

    @KafkaListener(
            topics = "product-updated-events",
            groupId = "synchronization-catalogue-group",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleUpdated(List<ProductUpdatedEvent> productUpdatedEvents) {
        productFacade.updateAll(productUpdatedEvents.stream()
                .map(ProductUpdatedEvent::getPayload)
                .toList());
    }

    @KafkaListener(
            topics = "product-deleted-events",
            groupId = "synchronization-catalogue-group",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleDeleted(List<ProductDeletedEvent> productDeletedEvents) {
        productFacade.deleteAll(productDeletedEvents.stream()
                .map(ProductDeletedEvent::getPayload)
                .map(ProductDTO::getId)
                .toList());
    }
}