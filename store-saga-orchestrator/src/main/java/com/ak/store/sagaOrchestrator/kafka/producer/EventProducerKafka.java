package com.ak.store.sagaOrchestrator.kafka.producer;

import com.ak.store.common.kafka.KafkaEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EventProducerKafka {
    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    public <T extends KafkaEvent> void send(T event, String topic, String key) {
        try {
            kafkaTemplate.send(topic, key, event).get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Kafka error sending to topic: " + topic, e);
        }
    }
}