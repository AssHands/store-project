package com.ak.store.synchronization.kafka.consumer;

import com.ak.store.common.kafka.catalogue.ProductCreatedEvent;
import com.ak.store.common.kafka.catalogue.ProductDeletedEvent;
import com.ak.store.common.kafka.catalogue.ProductRatingUpdatedEvent;
import com.ak.store.common.kafka.catalogue.ProductUpdatedEvent;
import com.ak.store.synchronization.errorHandler.ProductKafkaErrorHandler;
import com.ak.store.synchronization.facade.ProductFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductConsumerKafka {
    private final ProductFacade productFacade;
    private final ProductKafkaErrorHandler errorHandler;

    @KafkaListener(
            topics = "${kafka.topics.product-created}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleCreated(List<ProductCreatedEvent> productCreatedEvents, Acknowledgment ack) {
        for (var event : productCreatedEvents) {
            try {
                productFacade.createOne(event.getPayload());
            } catch (Exception e) {
                errorHandler.handleCreateError(event, e);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = "${kafka.topics.product-updated}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleUpdated(List<ProductUpdatedEvent> productUpdatedEvents, Acknowledgment ack) {
        for (var event : productUpdatedEvents) {
            try {
                productFacade.updateOne(event.getPayload());
            } catch (Exception e) {
                errorHandler.handleUpdateError(event, e);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = "${kafka.topics.product-rating-updated}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleRatingUpdated(List<ProductRatingUpdatedEvent> productRatingUpdatedEvents, Acknowledgment ack) {
        for (var event : productRatingUpdatedEvents) {
            try {
                productFacade.updateOneRating(event.getProductRating());
            } catch (Exception e) {
                errorHandler.handleRatingUpdateError(event, e);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = "${kafka.topics.product-deleted}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleDeleted(List<ProductDeletedEvent> productDeletedEvents, Acknowledgment ack) {
        for (var event : productDeletedEvents) {
            try {
                productFacade.deleteOne(event.getProductId());
            } catch (Exception e) {
                errorHandler.handleDeleteError(event, e);
            }
        }

        ack.acknowledge();
    }
}