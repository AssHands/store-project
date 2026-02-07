package com.ak.store.SynchronizationSagaWorker.kafka;

import com.ak.store.SynchronizationSagaWorker.model.inbox.InboxEventType;
import com.ak.store.SynchronizationSagaWorker.service.InboxEventWriterService;
import com.ak.store.kafka.storekafkastarter.util.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.event.saga.SagaRequestEvent;
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
            topics = "product-synchronization-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleProductSynchronization(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.PRODUCT_SYNCHRONIZATION);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), event.getSagaName(), InboxEventType.PRODUCT_SYNCHRONIZATION);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "cancel-product-synchronization-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleCancelProductSynchronization(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.CANCEL_PRODUCT_SYNCHRONIZATION);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), event.getSagaName(), InboxEventType.CANCEL_PRODUCT_SYNCHRONIZATION);
            }
        }

        ack.acknowledge();
    }
}