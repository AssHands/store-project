package com.ak.store.synchronization.kafka.consumer;

import com.ak.store.common.event.catalogue.ProductCreatedEvent;
import com.ak.store.common.event.catalogue.ProductDeletedEvent;
import com.ak.store.common.event.catalogue.ProductUpdatedEvent;
import com.ak.store.synchronization.errorHandler.ProductKafkaErrorHandler;
import com.ak.store.synchronization.facade.ProductFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductConsumerKafka {
    private final ProductFacade productFacade;
    private final ProductKafkaErrorHandler errorHandler;

    @KafkaListener(
            topics = "${kafka.topics.product-created}",
            groupId = "${kafka.group-id}",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleCreated(List<ProductCreatedEvent> productCreatedEvents) {
        for (var event : productCreatedEvents) {
            try {
                productFacade.createOne(event.getPayload());
            } catch (Exception e) {
                errorHandler.handleCreateError(event, e);
            }
        }
    }

    @KafkaListener(
            topics = "${kafka.topics.product-updated}",
            groupId = "${kafka.group-id}",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleUpdated(List<ProductUpdatedEvent> productUpdatedEvents) {
        for (var event : productUpdatedEvents) {
            try {
                productFacade.updateOne(event.getPayload());
            } catch (Exception e) {
                errorHandler.handleUpdateError(event, e);
            }
        }
    }

    @KafkaListener(
            topics = "${kafka.topics.product-deleted}",
            groupId = "${kafka.group-id}",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleDeleted(List<ProductDeletedEvent> productDeletedEvents) {
        for (var event : productDeletedEvents) {
            try {
                productFacade.deleteOne(event.getPayload().getProduct().getId());
            } catch (Exception e) {
                errorHandler.handleDeleteError(event, e);
            }
        }
    }
}