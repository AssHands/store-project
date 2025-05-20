package com.ak.store.emailSender.kafka;

import com.ak.store.common.event.consumer.ConsumerVerifyEvent;
import com.ak.store.emailSender.facade.EmailFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class UserConsumerKafka {
    private final EmailFacade emailFacade;

    //todo add batches
    @KafkaListener(topics = "${kafka.topics.consumer-verify}", groupId = "${kafka.group-id}")
    public void handle(ConsumerVerifyEvent consumerVerifyEvent) {
        CompletableFuture.runAsync(() -> emailFacade.sendVerification(
                consumerVerifyEvent.getEmail(), consumerVerifyEvent.getVerificationCode()));
    }
}