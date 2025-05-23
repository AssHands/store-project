package com.ak.store.synchronization.kafka;

import com.ak.store.common.event.catalogue.CategoryCreatedEvent;
import com.ak.store.common.event.catalogue.CategoryDeletedEvent;
import com.ak.store.common.event.catalogue.CategoryUpdatedEvent;
import com.ak.store.synchronization.facade.CategoryFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryConsumerKafka {
    private final CategoryFacade categoryFacade;

    @KafkaListener(
            topics = "${kafka.topics.category-created}",
            groupId = "${kafka.group-id}",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleCreated(List<CategoryCreatedEvent> categoryCreatedEvents) {
        categoryFacade.createAll(categoryCreatedEvents.stream()
                .map(CategoryCreatedEvent::getPayload)
                .toList());
    }

    @KafkaListener(
            topics = "${kafka.topics.category-updated}",
            groupId = "${kafka.group-id}",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleUpdated(List<CategoryUpdatedEvent> categoryUpdatedEvents) {
        categoryFacade.updateAll(categoryUpdatedEvents.stream()
                .map(CategoryUpdatedEvent::getPayload)
                .toList());
    }

    @KafkaListener(
            topics = "${kafka.topics.category-deleted}",
            groupId = "${kafka.group-id}",
            batch = "true",
            containerFactory = "batchFactory")
    public void handleDeleted(List<CategoryDeletedEvent> categoryDeletedEvents) {
        categoryFacade.deleteAll(categoryDeletedEvents.stream()
                .map(v -> v.getPayload().getCategory().getId())
                .toList());
    }
}