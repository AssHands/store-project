package com.ak.store.order.kafka;

import com.ak.store.common.event.KafkaEvent;
import com.ak.store.common.event.order.OrderCreatedEvent;
import com.ak.store.order.util.KafkaTopicRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OrderProducerKafka {
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
        String topic = topicRegistry.getDltTopicByEvent(event.getClass());


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