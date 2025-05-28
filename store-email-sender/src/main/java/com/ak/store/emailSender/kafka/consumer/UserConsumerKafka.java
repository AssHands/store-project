package com.ak.store.emailSender.kafka.consumer;

import com.ak.store.common.event.user.UserVerifyEvent;
import com.ak.store.common.model.order.snapshot.OrderSnapshot;
import com.ak.store.common.model.user.snapshot.UserVerifySnapshot;
import com.ak.store.emailSender.errorHandler.UserKafkaErrorHandler;
import com.ak.store.emailSender.facade.EmailFacade;
import com.ak.store.emailSender.inbox.InboxEventType;
import com.ak.store.emailSender.service.InboxEventWriterService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class UserConsumerKafka {
    private final InboxEventWriterService<UserVerifySnapshot> inboxEventWriterService;
    private final UserKafkaErrorHandler errorHandler;

    @KafkaListener(topics = "${kafka.topics.user-verify}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handle(List<UserVerifyEvent> userVerifyEvents) {
        for(var event : userVerifyEvents) {
            try {
                inboxEventWriterService.createOne(event.getEventId(), event.getUserVerify(), InboxEventType.USER_VERIFY);
            } catch (Exception e) {
                errorHandler.handleVerifyError(event, e);
            }
        }
    }
}