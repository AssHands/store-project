package com.ak.store.outboxScheduler.kafka.catalogue;

import com.ak.store.common.event.catalogue.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryProducerKafka {
    @Qualifier("categoryKafkaTemplate")
    private final KafkaTemplate<String, CategoryEvent> kafkaTemplate;

    public void send(CategoryCreatedEvent categoryCreatedEvent) {
        try {
            SendResult<String, CategoryEvent> future = kafkaTemplate.send("category-created-events",
                    categoryCreatedEvent.getCategory().getId().toString(), categoryCreatedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka category-created-events error");
        }
    }

    public void send(CategoryUpdatedEvent categoryUpdatedEvent) {
        try {
            SendResult<String, CategoryEvent> future = kafkaTemplate.send("category-updated-events",
                    categoryUpdatedEvent.getCategory().getId().toString(), categoryUpdatedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka category-updated-events error");
        }
    }

    public void send(CategoryDeletedEvent categoryCreatedEvent) {
        try {
            SendResult<String, CategoryEvent> future = kafkaTemplate.send("category-deleted-events",
                    categoryCreatedEvent.getCategory().getId().toString(), categoryCreatedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka category-deleted-events error");
        }
    }
}