package com.ak.store.emailSender.kafka.consumer;

import com.ak.store.common.kafka.user.UserCreationEvent;
import com.ak.store.common.kafka.user.VerifyUserEvent;
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
    public void handleVerifyUser(List<VerifyUserEvent> verifyUserEvents, Acknowledgment ack) {
        for(var event : verifyUserEvents) {
            try {
                inboxEventWriterService.createOne(event.getEventId(), event.getVerifyUser(), InboxEventType.VERIFY_USER);
            } catch (Exception e) {
                errorHandler.handleVerifyUserError(event, InboxEventType.VERIFY_USER, e);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = "${kafka.topics.user-created}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleUserCreated(List<UserCreationEvent> userCreationEvents, Acknowledgment ack) {
        for(var event : userCreationEvents) {
            try {
                inboxEventWriterService.createOne(event.getEventId(), event.getVerifyUser(), InboxEventType.USER_CREATED);
            } catch (Exception e) {
                errorHandler.handleUserCreatedError(event, InboxEventType.USER_CREATED, e);
            }
        }

        ack.acknowledge();
    }
}