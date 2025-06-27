package com.ak.store.orderOutbox.kafka;

import com.ak.store.orderOutbox.util.KafkaTopicRegistry;
import com.ak.store.common.kafka.KafkaEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EventProducerKafka {
    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;
    private final KafkaTopicRegistry topicRegistry;

    public <T extends KafkaEvent> void send(T event, String key) {
        String topic = topicRegistry.getTopicByEvent(event.getClass());

        if (topic == null) {
            throw new IllegalArgumentException("No topic configured for event class: " + event.getClass().getName());
        }

        try {
            kafkaTemplate.send(topic, key, event).get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Kafka error sending to topic: " + topic, e);
        }
    }

    public <T extends KafkaEvent> void send(T event, String topic, String key) {
        try {
            kafkaTemplate.send(topic, key, event).get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Kafka error sending to topic: " + topic, e);
        }
    }
}