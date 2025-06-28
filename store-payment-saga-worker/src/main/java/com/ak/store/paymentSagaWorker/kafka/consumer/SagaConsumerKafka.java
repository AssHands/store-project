package com.ak.store.paymentSagaWorker.kafka.consumer;

import com.ak.store.common.saga.SagaRequestEvent;
import com.ak.store.paymentSagaWorker.inbox.InboxEventType;
import com.ak.store.paymentSagaWorker.inbox.InboxEventWriterService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class SagaConsumerKafka {
    private final InboxEventWriterService inboxEventWriterService;

    @KafkaListener(
            topics = "payment-reserve-funds",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleReserveFunds(List<SagaRequestEvent> events, Acknowledgment ack) {
        for(var event : events) {
            inboxEventWriterService.createOne(event.getSagaId(), event.getRequest(), InboxEventType.RESERVE_FUNDS);
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "payment-release-funds",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleReleaseFunds(List<SagaRequestEvent> events, Acknowledgment ack) {
        for(var event : events) {
            inboxEventWriterService.createOne(event.getSagaId(), event.getRequest(), InboxEventType.RELEASE_FUNDS);
        }

        ack.acknowledge();
    }
}