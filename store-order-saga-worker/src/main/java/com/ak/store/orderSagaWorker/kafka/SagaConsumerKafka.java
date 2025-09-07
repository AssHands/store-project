package com.ak.store.orderSagaWorker.kafka;

import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.order.OrderCreation;
import com.ak.store.kafka.storekafkastarter.model.saga.SagaRequestEvent;
import com.ak.store.orderSagaWorker.model.inbox.InboxEventType;
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
    private final JsonMapperKafka jsonMapperKafka;

    @KafkaListener(
            topics = "confirm-order-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleConfirmOrder(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.CONFIRM_ORDER);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), event.getSagaName(), InboxEventType.CONFIRM_ORDER);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "cancel-order-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleCancelOrder(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), event.getRequest().toString(), InboxEventType.CANCEL_ORDER);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), event.getSagaName(), InboxEventType.CANCEL_ORDER);
            }
        }

        ack.acknowledge();
    }
}