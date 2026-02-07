package com.ak.store.reviewSagaWorker.kafka;

import com.ak.store.kafka.storekafkastarter.util.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.event.saga.SagaRequestEvent;
import com.ak.store.reviewSagaWorker.model.inbox.InboxEventType;
import com.ak.store.reviewSagaWorker.service.InboxEventWriterService;
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
            topics = "confirm-review-creation-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleConfirmReviewCreation(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.CONFIRM_REVIEW_CREATED);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), event.getSagaName(), InboxEventType.CONFIRM_REVIEW_CREATED);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "cancel-review-creation-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleCancelReviewCreation(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.CANCEL_REVIEW_CREATED);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), event.getSagaName(), InboxEventType.CANCEL_REVIEW_CREATED);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "confirm-review-update-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleConfirmReviewUpdate(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.CONFIRM_REVIEW_UPDATED);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), event.getSagaName(), InboxEventType.CONFIRM_REVIEW_UPDATED);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "cancel-review-update-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleCancelReviewUpdate(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.CANCEL_REVIEW_UPDATED);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), event.getSagaName(), InboxEventType.CANCEL_REVIEW_UPDATED);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "confirm-review-deletion-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleConfirmReviewDeletion(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.CONFIRM_REVIEW_DELETED);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), event.getSagaName(), InboxEventType.CONFIRM_REVIEW_DELETED);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "cancel-review-deletion-request",
            groupId = "${spring.kafka.consumer.group-id}",
            batch = "true"
    )
    public void handleCancelReviewDeletion(List<SagaRequestEvent> events, Acknowledgment ack) {
        for (var event : events) {
            try {
                inboxEventWriterService.createOne(event.getStepId(), event.getStepName(), event.getSagaId(),
                        event.getSagaName(), jsonMapperKafka.toJson(event.getRequest()), InboxEventType.CANCEL_REVIEW_DELETED);
            } catch (Exception e) {
                inboxEventWriterService.createOneFailure(event.getStepId(), event.getStepName(),
                        event.getSagaId(), event.getSagaName(), InboxEventType.CANCEL_REVIEW_DELETED);
            }
        }

        ack.acknowledge();
    }
}