package com.ak.store.emailSender.kafka;

import com.ak.store.common.event.consumer.ConsumerVerifyEvent;
import com.ak.store.emailSender.service.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class ConsumerConsumerKafka {
    private final EmailSender emailSender;

    //todo add batches
    @KafkaListener(topics = "consumer-verify-events", groupId = "email-consumer-group")
    public void handle(ConsumerVerifyEvent consumerVerifyEvent) {
        CompletableFuture.runAsync(() -> emailSender.sendVerificationEmail(
                consumerVerifyEvent.getEmail(), consumerVerifyEvent.getVerificationCode()));
    }
}