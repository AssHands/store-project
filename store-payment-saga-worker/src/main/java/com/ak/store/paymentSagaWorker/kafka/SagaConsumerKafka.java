package com.ak.store.paymentSagaWorker.kafka;


import com.ak.store.kafka.storekafkastarter.util.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.event.saga.SagaRequestEvent;
import com.ak.store.paymentSagaWorker.model.inbox.InboxEventType;
import com.ak.store.paymentSagaWorker.service.InboxEventWriterService;
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
            topics = "payment-reserve-funds-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleReserveFunds(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.RESERVE_FUNDS);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), event.getSagaName(), InboxEventType.RESERVE_FUNDS);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "payment-release-funds-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleReleaseFunds(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.RELEASE_FUNDS);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), event.getSagaName(), InboxEventType.RELEASE_FUNDS);
            }
        }

        ack.acknowledge();
    }

//    @KafkaListener(
//            topics = "user-payment-creation-request",
//            groupId = "${spring.kafka.consumer.group-id}",
//            batch = "true"
//    )
//    public void handleUserPaymentCreation(List<SagaRequestEvent<OrderCreation>> events, Acknowledgment ack) {
//        for (var event : events) {
//            try {
//                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
//                        event.getSagaName(), event.getRequest().toString(), InboxEventType.USER_PAYMENT_CREATION);
//            } catch (Exception e) {
//                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
//                        event.getSagaId(), event.getSagaName(), InboxEventType.USER_PAYMENT_CREATION);
//            }
//        }
//
//        ack.acknowledge();
//    }

//    @KafkaListener(
//            topics = "cancel-user-payment-creation-request",
//            groupId = "${spring.kafka.consumer.group-id}",
//            batch = "true"
//    )
//    public void handleCancelUserPaymentCreation(List<SagaRequestEvent<OrderCreation>> events, Acknowledgment ack) {
//        for (var event : events) {
//            try {
//                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
//                        event.getSagaName(), event.getRequest().toString(), InboxEventType.CANCEL_USER_PAYMENT_CREATION);
//            } catch (Exception e) {
//                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
//                        event.getSagaId(), event.getSagaName(), InboxEventType.CANCEL_USER_PAYMENT_CREATION);
//            }
//        }
//
//        ack.acknowledge();
//    }
}