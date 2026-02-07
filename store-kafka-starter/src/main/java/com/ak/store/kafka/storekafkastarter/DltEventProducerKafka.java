package com.ak.store.kafka.storekafkastarter;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class DltEventProducerKafka {
    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    public CompletableFuture<SendResult<String, KafkaEvent>> sendAsync(KafkaEvent event, String topic, String key) {
        return kafkaTemplate.send(topic + ".dlt", key, event);
    }

    public CompletableFuture<SendResult<String, KafkaEvent>> sendAsync(KafkaEvent event, String topic) {
        return kafkaTemplate.send(topic + ".dlt", event);
    }
}
