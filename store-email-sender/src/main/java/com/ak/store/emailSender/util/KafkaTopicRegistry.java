package com.ak.store.emailSender.util;

import com.ak.store.emailSender.inbox.InboxEventType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicRegistry {
    private final KafkaProperties kafkaProperties;
    private final Map<InboxEventType, String> eventTopicMap = new HashMap<>();

    @PostConstruct
    public void init() {
        Map<InboxEventType, String> eventKeyMap = Map.of(
                InboxEventType.VERIFY_USER, "verify-user",
                InboxEventType.USER_CREATED, "user-created"
        );

        for (var entry : eventKeyMap.entrySet()) {
            String topic = kafkaProperties.getTopicByKey(entry.getValue());
            if (topic == null) {
                throw new IllegalStateException("Missing topic in config for key: " + entry.getValue());
            }
            eventTopicMap.put(entry.getKey(), topic);
        }
    }

    public String getTopicByEvent(InboxEventType eventType) {
        String topic = eventTopicMap.get(eventType);
        if (topic == null) {
            throw new IllegalArgumentException("No topic configured for event: " + eventType.getValue());
        }
        return topic;
    }

    public String getDltTopicByEvent(InboxEventType eventClass) {
        String topic = eventTopicMap.get(eventClass);
        if (topic == null) {
            throw new IllegalArgumentException("No topic configured for event: " + eventClass.getValue());
        }

        return topic + kafkaProperties.getDltPrefix();
    }
}