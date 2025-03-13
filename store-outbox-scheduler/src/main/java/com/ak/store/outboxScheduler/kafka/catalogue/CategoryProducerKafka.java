package com.ak.store.outboxScheduler.kafka.catalogue;

import com.ak.store.common.event.catalogue.CategoryCreatedEvent;
import com.ak.store.common.event.catalogue.CategoryEvent;
import com.ak.store.common.event.catalogue.ProductDeletedEvent;
import com.ak.store.common.event.catalogue.ProductEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryProducerKafka {
    @Qualifier("categoryKafkaTemplate")
    private final KafkaTemplate<Long, CategoryEvent> kafkaTemplate;

    public void send(CategoryCreatedEvent categoryCreatedEvent) {
        try {
            SendResult<Long, CategoryEvent> future = kafkaTemplate.send(
                    "product-deleted-events", categoryCreatedEvent.getCategory().getId(), categoryCreatedEvent).get();
        } catch (Exception e) {
            throw new RuntimeException("kafka category-created-events error");
        }
    }
}
