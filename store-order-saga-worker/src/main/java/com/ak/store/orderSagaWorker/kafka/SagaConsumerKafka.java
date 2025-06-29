package com.ak.store.orderSagaWorker.kafka;

import com.ak.store.common.saga.SagaRequestEvent;
import com.ak.store.orderSagaWorker.model.entity.InboxEventType;
import com.ak.store.orderSagaWorker.service.InboxEventWriterService;
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
            topics = "confirm-order-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleConfirmOrder(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            inboxEventWriterService.createOne(event.getSagaId(), event.getStepName(),
                    event.getRequest().toString(), InboxEventType.CONFIRM_ORDER);
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "cancel-order-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleCanselOrder(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            inboxEventWriterService.createOne(event.getSagaId(), event.getStepName(),
                    event.getRequest().toString(), InboxEventType.CANCEL_ORDER);
        }

        ack.acknowledge();
    }
}