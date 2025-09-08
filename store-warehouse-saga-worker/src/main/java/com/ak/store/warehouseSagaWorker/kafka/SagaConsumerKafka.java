package com.ak.store.warehouseSagaWorker.kafka;


import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.event.saga.SagaRequestEvent;
import com.ak.store.warehouseSagaWorker.model.inbox.InboxEventType;
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
    private final JsonMapperKafka jsonMapperKafka;

    @KafkaListener(
            topics = "warehouse-reserve-products-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleReserveProducts(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.RESERVE_PRODUCTS);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.RESERVE_PRODUCTS);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "warehouse-release-products-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleReleaseProducts(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.RELEASE_PRODUCTS);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.RELEASE_PRODUCTS);
            }
        }

        ack.acknowledge();
    }

    //-------------------------------------------------------------

//    @KafkaListener(
//            topics = "inventory-creation-request",
//            groupId = "${spring.kafka.consumer.group-id}",
//            batch = "true"
//    )
//    public void handleInventoryCreation(List<SagaRequestEvent> events, Acknowledgment ack) {
//        for (var event : events) {
//            try {
//                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
//                        event.getSagaName(), event.getRequest().toString(), InboxEventType.INVENTORY_CREATION);
//            } catch (Exception e) {
//                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
//                        event.getSagaId(), event.getSagaName(), InboxEventType.INVENTORY_CREATION);
//            }
//        }
//
//        ack.acknowledge();
//    }
//
//    @KafkaListener(
//            topics = "cancel-inventory-creation-request",
//            groupId = "${spring.kafka.consumer.group-id}",
//            batch = "true"
//    )
//    public void handleCancelInventoryCreation(List<SagaRequestEvent> events, Acknowledgment ack) {
//        for (var event : events) {
//            try {
//                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
//                        event.getSagaName(), event.getRequest().toString(), InboxEventType.CANCEL_INVENTORY_CREATION);
//            } catch (Exception e) {
//                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
//                        event.getSagaId(), event.getSagaName(), InboxEventType.CANCEL_INVENTORY_CREATION);
//            }
//        }
//
//        ack.acknowledge();
//    }
}