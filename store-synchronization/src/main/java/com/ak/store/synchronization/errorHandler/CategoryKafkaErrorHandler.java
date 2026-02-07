package com.ak.store.synchronization.errorHandler;

import com.ak.store.kafka.storekafkastarter.DltEventProducerKafka;
import com.ak.store.kafka.storekafkastarter.KafkaEvent;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.category.CategoryCreatedEvent;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.category.CategoryDeletedEvent;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.category.CategoryUpdatedEvent;
import com.ak.store.synchronization.util.KafkaTopicRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryKafkaErrorHandler {
    private final DltEventProducerKafka dltEventProducerKafka;
    private final KafkaTopicRegistry topicRegistry;

    public void handleCreateError(CategoryCreatedEvent event, Exception e) {
        String key = event.getPayload().getCategory().getId().toString();
        dltEventProducerKafka.sendAsync(event, getTopicByEvent(event), key);
    }

    public void handleUpdateError(CategoryUpdatedEvent event, Exception e) {
        String key = event.getPayload().getCategory().getId().toString();
        dltEventProducerKafka.sendAsync(event, getTopicByEvent(event), key);
    }

    public void handleDeleteError(CategoryDeletedEvent event, Exception e) {
        String key = event.getCategoryId().toString();
        dltEventProducerKafka.sendAsync(event, getTopicByEvent(event), key);
    }

    private String getTopicByEvent(KafkaEvent event) {
        String topic = topicRegistry.getTopicByEvent(event.getClass());

        if (topic == null) {
            throw new IllegalArgumentException("No topic configured for event class: " + event.getClass().getName());
        }

        return topic;
    }
}
