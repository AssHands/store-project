package com.ak.store.emailSender.errorHandler;

import com.ak.store.common.kafka.user.UserCreatedEvent;
import com.ak.store.common.kafka.user.VerifyUserEvent;
import com.ak.store.emailSender.inbox.InboxEventType;
import com.ak.store.emailSender.kafka.producer.DltProducerKafka;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserKafkaErrorHandler {
    private final DltProducerKafka dltProducerKafka;

    public void handleVerifyUserError(VerifyUserEvent event, InboxEventType eventType, Exception e) {
        dltProducerKafka.send(event, eventType);
    }

    public void handleUserCreatedError(UserCreatedEvent event, InboxEventType eventType, Exception e) {
        dltProducerKafka.send(event, eventType);
    }
}
