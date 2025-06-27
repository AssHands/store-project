package com.ak.store.emailSender.kafka.consumer;

import com.ak.store.common.kafka.user.UserVerifyEvent;
import com.ak.store.emailSender.errorHandler.UserKafkaErrorHandler;
import com.ak.store.emailSender.inbox.InboxEventType;
import com.ak.store.emailSender.service.InboxEventWriterService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserConsumerKafka {
    private final InboxEventWriterService inboxEventWriterService;
    private final UserKafkaErrorHandler errorHandler;

    @KafkaListener(topics = "${kafka.topics.user-verify}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handle(List<UserVerifyEvent> userVerifyEvents, Acknowledgment ack) {
        for(var event : userVerifyEvents) {
            try {
                inboxEventWriterService.createOne(event.getEventId(), event.getUserVerify(), InboxEventType.USER_VERIFY);
            } catch (Exception e) {
                errorHandler.handleVerifyError(event, e);
            }
        }

        ack.acknowledge();
    }
}