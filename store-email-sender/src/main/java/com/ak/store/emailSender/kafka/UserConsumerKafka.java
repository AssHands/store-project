package com.ak.store.emailSender.kafka;

import com.ak.store.emailSender.errorHandler.ErrorHandler;
import com.ak.store.emailSender.inbox.InboxEventType;
import com.ak.store.emailSender.service.InboxEventWriterService;
import com.ak.store.kafka.storekafkastarter.model.event.user.UserVerificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserConsumerKafka {
    private final InboxEventWriterService inboxEventWriterService;
    private final ErrorHandler errorHandler;

    @KafkaListener(topics = "${kafka.topics.user-verification}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleVerifyUser(List<UserVerificationEvent> userVerificationEvents, Acknowledgment ack) {
        for (var event : userVerificationEvents) {
            try {
                inboxEventWriterService.createOne(event.getEventId(), event.getPayload(), InboxEventType.USER_VERIFICATION);
            } catch (Exception e) {
                errorHandler.handleVerificationError(event, e);
            }
        }

        ack.acknowledge();
    }
}