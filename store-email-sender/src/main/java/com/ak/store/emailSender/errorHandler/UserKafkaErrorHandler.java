package com.ak.store.emailSender.errorHandler;

import com.ak.store.common.kafka.user.UserVerifyEvent;
import com.ak.store.emailSender.kafka.producer.DltProducerKafka;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserKafkaErrorHandler {
    private final DltProducerKafka dltProducerKafka;

    public void handleVerifyError(UserVerifyEvent event, Exception e) {
        dltProducerKafka.send(event);
    }
}
