package com.ak.store.synchronization.kafka.consumer;

import com.ak.store.common.event.catalogue.CategoryCreatedEvent;
import com.ak.store.common.event.catalogue.CategoryDeletedEvent;
import com.ak.store.common.event.catalogue.CategoryUpdatedEvent;
import com.ak.store.synchronization.errorHandler.CategoryKafkaErrorHandler;
import com.ak.store.synchronization.facade.CategoryFacade;
import com.ak.store.synchronization.util.KafkaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryConsumerKafka {
    private final CategoryFacade categoryFacade;
    private final CategoryKafkaErrorHandler errorHandler;

    @KafkaListener(
            topics = "${kafka.topics.category-created}",
            groupId = "${kafka.group-id}",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleCreated(List<CategoryCreatedEvent> categoryCreatedEvents) {
        for(var event : categoryCreatedEvents) {
            try {
                categoryFacade.createOne(event.getPayload());
            } catch (Exception e) {
                errorHandler.handleCreateError(event, e);
            }
        }
    }

    @KafkaListener(
            topics = "${kafka.topics.category-updated}",
            groupId = "${kafka.group-id}",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleUpdated(List<CategoryUpdatedEvent> categoryUpdatedEvents) {
        for(var event : categoryUpdatedEvents) {
            try {
                categoryFacade.updateOne(event.getPayload());
            } catch (Exception e) {
                errorHandler.handleUpdateError(event, e);
            }
        }
    }

    @KafkaListener(
            topics = "${kafka.topics.category-deleted}",
            groupId = "${kafka.group-id}",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleDeleted(List<CategoryDeletedEvent> categoryDeletedEvents) {
        for(var event : categoryDeletedEvents) {
            try {
                categoryFacade.deleteOne(event.getPayload().getCategory().getId());
            } catch (Exception e) {
                errorHandler.handleDeleteError(event, e);
            }
        }
    }
}