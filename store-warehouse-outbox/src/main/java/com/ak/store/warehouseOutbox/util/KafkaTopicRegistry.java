package com.ak.store.warehouseOutbox.util;

import com.ak.store.warehouseOutbox.model.OutboxEventType;
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
        Map<OutboxEventType, String> eventKeyMap = Map.of(
                OutboxEventType.RESERVE_PRODUCTS, "reserve-products",
                OutboxEventType.RELEASE_PRODUCTS, "release-products"
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
