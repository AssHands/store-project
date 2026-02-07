package com.ak.store.search.util;

import com.ak.store.kafka.storekafkastarter.model.KafkaEvent;
import com.ak.store.kafka.storekafkastarter.model.event.search.SearchEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicRegistry {
    private final KafkaProperties kafkaProperties;
    private final Map<Class<? extends KafkaEvent>, String> eventTopicMap = new HashMap<>();

    @PostConstruct
    public void init() {
        Map<Class<? extends KafkaEvent>, String> eventKeyMap = Map.of(
                SearchEvent.class, "search-events"
        );

        for (var entry : eventKeyMap.entrySet()) {
            String topic = kafkaProperties.getTopicByKey(entry.getValue());
            if (topic == null) {
                throw new IllegalStateException("Missing topic in config for key: " + entry.getValue());
            }
            eventTopicMap.put(entry.getKey(), topic);
        }
    }

    public String getTopicByEvent(Class<? extends KafkaEvent> eventClass) {
        String topic = eventTopicMap.get(eventClass);
        if (topic == null) {
            throw new IllegalArgumentException("No topic configured for event: " + eventClass.getName());
        }
        return topic;
    }
}