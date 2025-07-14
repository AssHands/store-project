package com.ak.store.userOutbox.util;

import com.ak.store.userOutbox.model.OutboxEventType;
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
                OutboxEventType.USER_REGISTRATION, "user-registration",
                OutboxEventType.CANCEL_USER_REGISTRATION, "cancel-user-registration",
                OutboxEventType.USER_CREATION, "user-creation",
                OutboxEventType.CONFIRM_USER_CREATION, "confirm-user",
                OutboxEventType.CANCEL_USER_CREATION, "cancel-user-creation",
                OutboxEventType.USER_VERIFICATION, "user-verification"
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
