package com.ak.store.synchronization.errorHandler;

import com.ak.store.kafka.storekafkastarter.DltEventProducerKafka;
import com.ak.store.kafka.storekafkastarter.model.KafkaEvent;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.product.ProductDeletedEvent;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.product.ProductRatingUpdatedEvent;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.product.ProductUpdatedEvent;
import com.ak.store.synchronization.util.KafkaTopicRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductKafkaErrorHandler {
    private final DltEventProducerKafka dltEventProducerKafka;
    private final KafkaTopicRegistry topicRegistry;

    public void handleUpdateError(ProductUpdatedEvent event, Exception e) {
        String key = event.getPayload().getProduct().getId().toString();
        dltEventProducerKafka.sendAsync(event, getTopicByEvent(event), key);
    }

    public void handleDeleteError(ProductDeletedEvent event, Exception e) {
        String key = event.getProductId().toString();
        dltEventProducerKafka.sendAsync(event, getTopicByEvent(event), key);
    }

    public void handleRatingUpdateError(ProductRatingUpdatedEvent event, Exception e) {
        String key = event.getPayload().getId().toString();
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