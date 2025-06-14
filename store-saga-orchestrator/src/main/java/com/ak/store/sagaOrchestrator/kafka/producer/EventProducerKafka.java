package com.ak.store.sagaOrchestrator.kafka.producer;

import com.ak.store.common.event.KafkaEvent;
import com.ak.store.common.saga.SagaEvent;
import com.ak.store.sagaOrchestrator.util.KafkaTopicRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class EventProducerKafka {
    private final KafkaTemplate<String, Object> kafkaTemplate;
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

    public <T extends KafkaEvent> void send(T event) {
        String topic = topicRegistry.getTopicByEvent(event.getClass());

        if (topic == null) {
            throw new IllegalArgumentException("No topic configured for event class: " + event.getClass().getName());
        }

        try {
            kafkaTemplate.send(topic, event).get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Kafka error sending to topic: " + topic, e);
        }
    }
}