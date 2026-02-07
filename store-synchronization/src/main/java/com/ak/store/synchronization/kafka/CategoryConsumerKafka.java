package com.ak.store.synchronization.kafka;

import com.ak.store.kafka.storekafkastarter.model.event.catalogue.category.CategoryCreatedEvent;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.category.CategoryDeletedEvent;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.category.CategoryUpdatedEvent;
import com.ak.store.synchronization.errorHandler.CategoryKafkaErrorHandler;
import com.ak.store.synchronization.mapper.CategoryMapper;
import com.ak.store.synchronization.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryConsumerKafka {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final CategoryKafkaErrorHandler errorHandler;

    @KafkaListener(topics = "${kafka.topics.category-created}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleCreated(List<CategoryCreatedEvent> categoryCreatedEvents, Acknowledgment ack) {
        for(var event : categoryCreatedEvents) {
            try {
                categoryService.createOne(categoryMapper.toWritePayloadCommand(event.getPayload()));
            } catch (Exception e) {
                errorHandler.handleCreateError(event, e);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = "${kafka.topics.category-updated}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleUpdated(List<CategoryUpdatedEvent> categoryUpdatedEvents, Acknowledgment ack) {
        for(var event : categoryUpdatedEvents) {
            try {
                categoryService.updateOne(categoryMapper.toWritePayloadCommand(event.getPayload()));
            } catch (Exception e) {
                errorHandler.handleUpdateError(event, e);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = "${kafka.topics.category-deleted}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleDeleted(List<CategoryDeletedEvent> categoryDeletedEvents, Acknowledgment ack) {
        for(var event : categoryDeletedEvents) {
            try {
                categoryService.deleteOne(event.getCategoryId());
            } catch (Exception e) {
                errorHandler.handleDeleteError(event, e);
            }
        }

        ack.acknowledge();
    }
}