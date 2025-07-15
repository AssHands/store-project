package com.ak.store.catalogueOutbox.util;

import com.ak.store.catalogueOutbox.model.OutboxEventType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicRegistry {
    private final KafkaProperties kafkaProperties;
    private final Map<OutboxEventType, String> eventTopicMap = new HashMap<>();

    @PostConstruct
    public void init() {
        Map<OutboxEventType, String> eventKeyMap = Map.ofEntries(
                Map.entry(OutboxEventType.CATEGORY_CREATED, "category-created"),
                Map.entry(OutboxEventType.CATEGORY_UPDATED, "category-updated"),
                Map.entry(OutboxEventType.CATEGORY_DELETED, "category-deleted"),

                Map.entry(OutboxEventType.CHARACTERISTIC_CREATED, "characteristic-created"),
                Map.entry(OutboxEventType.CHARACTERISTIC_UPDATED, "characteristic-updated"),
                Map.entry(OutboxEventType.CHARACTERISTIC_DELETED, "characteristic-deleted"),

                Map.entry(OutboxEventType.PRODUCT_CREATION, "product-creation"),
                Map.entry(OutboxEventType.CANCEL_PRODUCT_CREATION, "cancel-product-creation"),
                Map.entry(OutboxEventType.CONFIRM_PRODUCT_CREATION, "confirm-product-creation"),

                Map.entry(OutboxEventType.PRODUCT_UPDATED, "product-updated"),
                Map.entry(OutboxEventType.PRODUCT_DELETED, "product-deleted"),


                Map.entry(OutboxEventType.ADD_PRODUCT_GRADE, "add-product-grade"),
                Map.entry(OutboxEventType.REMOVE_PRODUCT_GRADE, "remove-product-grade"),

                Map.entry(OutboxEventType.UPDATE_PRODUCT_GRADE, "update-product-grade"),
                Map.entry(OutboxEventType.CANCEL_UPDATE_PRODUCT_GRADE, "cancel-update-product-grade"),

                Map.entry(OutboxEventType.DELETE_PRODUCT_GRADE, "delete-product-grade"),
                Map.entry(OutboxEventType.CANCEL_DELETE_PRODUCT_GRADE, "cancel-delete-product-grade")

        );

        for (var entry : eventKeyMap.entrySet()) {
            String topic = kafkaProperties.getTopicByKey(entry.getValue());
            if (topic == null) {
                throw new IllegalStateException("Missing topic in config for key: " + entry.getValue());
            }
            eventTopicMap.put(entry.getKey(), topic);
        }
    }

    public String getTopicByEvent(OutboxEventType eventType) {
        String topic = eventTopicMap.get(eventType);
        if (topic == null) {
            throw new IllegalArgumentException("No topic configured for event: " + eventType.getValue());
        }
        return topic;
    }
}
