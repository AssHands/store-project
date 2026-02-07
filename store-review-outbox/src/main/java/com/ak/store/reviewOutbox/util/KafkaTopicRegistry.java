package com.ak.store.reviewOutbox.util;

import com.ak.store.reviewOutbox.model.OutboxEventType;
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
                Map.entry(OutboxEventType.REVIEW_CREATED, "review-created"),
                Map.entry(OutboxEventType.CONFIRM_REVIEW_CREATED, "confirm-review-created"),
                Map.entry(OutboxEventType.CANCEL_REVIEW_CREATED, "cancel-review-created"),

                Map.entry(OutboxEventType.REVIEW_UPDATED, "review-updated"),
                Map.entry(OutboxEventType.CONFIRM_REVIEW_UPDATED, "confirm-review-updated"),
                Map.entry(OutboxEventType.CANCEL_REVIEW_UPDATED, "cancel-review-updated"),

                Map.entry(OutboxEventType.REVIEW_DELETED, "review-deleted"),
                Map.entry(OutboxEventType.CONFIRM_REVIEW_DELETED, "confirm-review-deleted"),
                Map.entry(OutboxEventType.CANCEL_REVIEW_DELETED, "cancel-review-deleted")
        );

        for (var entry : eventKeyMap.entrySet()) {
            String topic = kafkaProperties.getTopicByKey(entry.getValue());
            if (topic == null) {
                throw new IllegalStateException("Missing topic in config for key: " + entry.getValue());
            }
            eventTopicMap.put(entry.getKey(), topic);
        }
    }

    public String getTopicByEvent(OutboxEventType type) {
        String topic = eventTopicMap.get(type);
        if (topic == null) {
            throw new IllegalArgumentException("No topic configured for event: " + type.getValue());
        }
        return topic;
    }
}