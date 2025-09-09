package com.ak.store.emailSender.errorHandler;

import com.ak.store.emailSender.inbox.InboxEventType;
import com.ak.store.emailSender.kafka.producer.DltProducerKafka;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ErrorHandler {
    private final DltProducerKafka dltProducerKafka;

    public void handleError(UserVerificationEvent event, InboxEventType eventType, Exception e) {
        dltProducerKafka.send(event, eventType);
    }
}
