package com.ak.store.outboxScheduler.kafka.catalogue;

import com.ak.store.common.event.catalogue.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryProducerKafka {
    @Qualifier("categoryKafkaTemplate")
    private final KafkaTemplate<String, CategoryEvent> kafkaTemplate;

    @Value("${kafka.topics.category-created}")
    private String CATEGORY_CREATED_TOPIC;

    @Value("${kafka.topics.category-updated}")
    private String CATEGORY_UPDATED_TOPIC;

    @Value("${kafka.topics.category-deleted}")
    private String CATEGORY_DELETED_TOPIC;

    public void send(CategoryCreatedEvent categoryCreatedEvent) {
        try {
            SendResult<String, CategoryEvent> future = kafkaTemplate.send(CATEGORY_CREATED_TOPIC,
                    categoryCreatedEvent.getPayload().getCategory().getId().toString(), categoryCreatedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka" + CATEGORY_CREATED_TOPIC + "error");
        }
    }

    public void send(CategoryUpdatedEvent categoryUpdatedEvent) {
        try {
            SendResult<String, CategoryEvent> future = kafkaTemplate.send(CATEGORY_UPDATED_TOPIC,
                    categoryUpdatedEvent.getPayload().getCategory().getId().toString(), categoryUpdatedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka" + CATEGORY_UPDATED_TOPIC + "error");
        }
    }

    public void send(CategoryDeletedEvent categoryDeletedEvent) {
        try {
            SendResult<String, CategoryEvent> future = kafkaTemplate.send(CATEGORY_DELETED_TOPIC,
                    categoryDeletedEvent.getPayload().getCategory().getId().toString(), categoryDeletedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka" + CATEGORY_DELETED_TOPIC + "error");
        }
    }
}