package com.ak.store.emailSender.errorHandler;

import com.ak.store.emailSender.util.KafkaTopicRegistry;
import com.ak.store.kafka.storekafkastarter.DltEventProducerKafka;
import com.ak.store.kafka.storekafkastarter.model.KafkaEvent;
import com.ak.store.kafka.storekafkastarter.model.event.user.UserVerificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ErrorHandler {
    private final DltEventProducerKafka dltEventProducerKafka;
    private final KafkaTopicRegistry topicRegistry;

    public void handleVerificationError(UserVerificationEvent event, Exception e) {
        String key = event.getPayload().getUserId().toString();
        dltEventProducerKafka.sendAsync(event, getTopicByEvent(event), key);
    }

    private String getTopicByEvent(KafkaEvent event) {
        String topic = topicRegistry.getTopicByEvent(event.getClass());

        if (topic == null) {
            throw new IllegalArgumentException("No topic configured for event class: " + event.getClass().getName());
        }

        return topic;
    }
}
