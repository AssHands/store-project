package com.ak.store.synchronization.kafka;

import com.ak.store.kafka.storekafkastarter.model.event.catalogue.product.ProductDeletedEvent;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.product.ProductRatingUpdatedEvent;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.product.ProductUpdatedEvent;
import com.ak.store.synchronization.errorHandler.ProductKafkaErrorHandler;
import com.ak.store.synchronization.mapper.ProductMapper;
import com.ak.store.synchronization.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductConsumerKafka {
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ProductKafkaErrorHandler errorHandler;

    @KafkaListener(topics = "${kafka.topics.product-updated}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleUpdated(List<ProductUpdatedEvent> productUpdatedEvents, Acknowledgment ack) {
        for (var event : productUpdatedEvents) {
            try {
                productService.updateOne(productMapper.toWritePayloadCommand(event.getPayload()));
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
                productService.updateOneRating(productMapper.toWriteRatingCommand(event.getPayload()));
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
                productService.deleteOne(event.getProductId());
            } catch (Exception e) {
                errorHandler.handleDeleteError(event, e);
            }
        }

        ack.acknowledge();
    }
}