package com.ak.store.paymentSagaWorker.processor.outbox.impl;

import com.ak.store.common.saga.SagaResponseEvent;
import com.ak.store.common.saga.SagaResponseStatus;
import com.ak.store.paymentSagaWorker.model.inbox.InboxEvent;
import com.ak.store.paymentSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.paymentSagaWorker.model.inbox.InboxEventType;
import com.ak.store.paymentSagaWorker.model.outbox.OutboxEventType;
import com.ak.store.paymentSagaWorker.processor.outbox.OutboxEventProcessor;
import com.ak.store.paymentSagaWorker.service.InboxEventReaderService;
import com.ak.store.paymentSagaWorker.service.OutboxEventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserPaymentCreationOutboxEventProcessor implements OutboxEventProcessor {
    private final InboxEventReaderService inboxEventReaderService;
    private final OutboxEventService outboxEventService;

    @Transactional
    @Override
    public void process(InboxEvent event) {
        SagaResponseStatus status;

        if(event.getStatus() == InboxEventStatus.SUCCESS) {
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
            outboxEventService.createOne(event.getId(), response, OutboxEventType.USER_PAYMENT_CREATION);
            inboxEventReaderService.markOneAs(event, InboxEventStatus.COMPLETED);
        } catch (Exception ignored) {
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.USER_PAYMENT_CREATION;
    }
}
