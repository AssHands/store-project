package com.ak.store.paymentOutbox.util;

import com.ak.store.paymentOutbox.model.OutboxEventType;
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
                OutboxEventType.RESERVE_FUNDS, "reserve-funds",
                OutboxEventType.RELEASE_FUNDS, "release-funds",
                OutboxEventType.USER_PAYMENT_CREATION, "user-payment-creation",
                OutboxEventType.CANCEL_USER_PAYMENT_CREATION, "cancel-user-payment-creation"
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
