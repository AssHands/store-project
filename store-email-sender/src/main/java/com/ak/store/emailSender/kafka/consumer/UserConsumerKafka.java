package com.ak.store.emailSender.kafka.consumer;

import com.ak.store.common.event.user.UserVerifyEvent;
import com.ak.store.emailSender.errorHandler.UserKafkaErrorHandler;
import com.ak.store.emailSender.facade.EmailFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class UserConsumerKafka {
    private final EmailFacade emailFacade;
    private final UserKafkaErrorHandler errorHandler;

    @KafkaListener(topics = "${kafka.topics.user-verify}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handle(List<UserVerifyEvent> userVerifyEvents) {
        for(var event : userVerifyEvents) {
            try {
                CompletableFuture.runAsync(() -> emailFacade.sendVerification(
                        event.getEmail(), event.getVerificationCode())).get(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                errorHandler.handleVerifyError(event, e);
            }
        }
    }
}