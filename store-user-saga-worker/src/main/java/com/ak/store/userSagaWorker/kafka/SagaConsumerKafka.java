package com.ak.store.userSagaWorker.kafka;

import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.event.saga.SagaRequestEvent;
import com.ak.store.userSagaWorker.model.inbox.InboxEventType;
import com.ak.store.userSagaWorker.service.InboxEventWriterService;
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
            topics = "cancel-user-registration-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleCancelUserRegistration(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.CANCEL_USER_REGISTRATION);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), event.getSagaName(), InboxEventType.CANCEL_USER_REGISTRATION);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "user-creation-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleUserCreation(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.USER_CREATION);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), event.getSagaName(), InboxEventType.USER_CREATION);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "cancel-user-creation-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleReleaseProducts(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.CANCEL_USER_CREATION);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), event.getSagaName(), InboxEventType.CANCEL_USER_CREATION);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "confirm-user-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleConfirmUserCreation(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.CONFIRM_USER_CREATION);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), event.getSagaName(), InboxEventType.CONFIRM_USER_CREATION);
            }
        }

        ack.acknowledge();
    }
}