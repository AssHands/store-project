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
                Map.entry(OutboxEventType.REVIEW_CREATION, "review-creation"),
                Map.entry(OutboxEventType.CONFIRM_REVIEW_CREATION, "confirm-review-creation"),
                Map.entry(OutboxEventType.CANCEL_REVIEW_CREATION, "cancel-review-creation"),

                Map.entry(OutboxEventType.REVIEW_UPDATE, "review-update"),
                Map.entry(OutboxEventType.CONFIRM_REVIEW_UPDATE, "confirm-review-update"),
                Map.entry(OutboxEventType.CANCEL_REVIEW_UPDATE, "cancel-review-update"),

                Map.entry(OutboxEventType.REVIEW_DELETION, "review-deletion"),
                Map.entry(OutboxEventType.CONFIRM_REVIEW_DELETION, "confirm-review-deletion"),
                Map.entry(OutboxEventType.CANCEL_REVIEW_DELETION, "cancel-review-deletion")
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