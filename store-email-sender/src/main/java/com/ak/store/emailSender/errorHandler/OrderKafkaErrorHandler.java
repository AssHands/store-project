package com.ak.store.emailSender.errorHandler;

import com.ak.store.common.kafka.order.OrderCreatedEvent;
import com.ak.store.emailSender.kafka.producer.DltProducerKafka;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderKafkaErrorHandler {
    private final DltProducerKafka dltProducerKafka;

    public void handleCreateError(OrderCreatedEvent event, Exception e) {
        dltProducerKafka.send(event);
    }
}
