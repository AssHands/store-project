package com.ak.store.warehouseSagaWorker.kafka;

import com.ak.store.common.saga.SagaRequestEvent;
import com.ak.store.warehouseSagaWorker.model.entity.InboxEventType;
import com.ak.store.warehouseSagaWorker.service.InboxEventWriterService;
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
            topics = "warehouse-reserve-products-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleReserveFunds(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            inboxEventWriterService.createOne(event.getSagaId(), event.getStepName(),
                    event.getRequest().toString(), InboxEventType.RESERVE_PRODUCTS);
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "warehouse-release-products-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleReleaseFunds(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            inboxEventWriterService.createOne(event.getSagaId(), event.getStepName(),
                    event.getRequest().asText(), InboxEventType.RELEASE_PRODUCTS);
        }

        ack.acknowledge();
    }
}