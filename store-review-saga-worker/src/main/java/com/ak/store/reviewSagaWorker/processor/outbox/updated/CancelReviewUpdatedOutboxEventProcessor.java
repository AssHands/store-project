package com.ak.store.reviewSagaWorker.processor.outbox.updated;

import com.ak.store.kafka.storekafkastarter.model.event.saga.SagaResponseEvent;
import com.ak.store.kafka.storekafkastarter.model.event.saga.SagaResponseStatus;
import com.ak.store.reviewSagaWorker.model.inbox.InboxEvent;
import com.ak.store.reviewSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.reviewSagaWorker.model.inbox.InboxEventType;
import com.ak.store.reviewSagaWorker.model.outbox.OutboxEventType;
import com.ak.store.reviewSagaWorker.processor.outbox.OutboxEventProcessor;
import com.ak.store.reviewSagaWorker.service.InboxEventReaderService;
import com.ak.store.reviewSagaWorker.service.OutboxEventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelReviewUpdatedOutboxEventProcessor implements OutboxEventProcessor {
    private final OutboxEventService outboxEventService;
    private final InboxEventReaderService inboxEventReaderService;

    @Transactional
    @Override
    public void process(InboxEvent event) {
        SagaResponseStatus status;

        if (event.getStatus() == InboxEventStatus.SUCCESS) {
            status = SagaResponseStatus.SUCCESS;
        } else {
            status = SagaResponseStatus.FAILURE;
        }

        var response = SagaResponseEvent.builder()
                .stepId(event.getId())
                .stepName(event.getStepName())
                .sagaId(event.getSagaId())
                .sagaName(event.getSagaName())
                .status(status)
                .build();

        try {
            outboxEventService.createOne(event.getId(), response, OutboxEventType.CANCEL_REVIEW_UPDATED);
            inboxEventReaderService.markOneAs(event, InboxEventStatus.COMPLETED);
        } catch (Exception ignored) {
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_REVIEW_UPDATED;
    }
}
