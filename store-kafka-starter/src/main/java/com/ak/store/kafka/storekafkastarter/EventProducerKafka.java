package com.ak.store.kafka.storekafkastarter;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EventProducerKafka {
    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    public CompletableFuture<SendResult<String, KafkaEvent>> sendAsync(KafkaEvent event, String topic, String key) {
        return kafkaTemplate.send(topic, key, event);
    }

    public <T extends KafkaEvent> CompletableFuture<SendResult<String, KafkaEvent>> sendAsync(T event, String topic) {
        return kafkaTemplate.send(topic, event);
    }
}