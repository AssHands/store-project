package com.ak.store.paymentSagaWorker.processor.outbox.impl;

import com.ak.store.kafka.storekafkastarter.model.saga.SagaResponseEvent;
import com.ak.store.kafka.storekafkastarter.model.saga.SagaResponseStatus;
import com.ak.store.paymentSagaWorker.model.inbox.InboxEvent;
import com.ak.store.paymentSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.paymentSagaWorker.model.inbox.InboxEventType;
import com.ak.store.paymentSagaWorker.service.InboxEventReaderService;
import com.ak.store.paymentSagaWorker.service.OutboxEventService;
import com.ak.store.paymentSagaWorker.model.outbox.OutboxEventType;
import com.ak.store.paymentSagaWorker.processor.outbox.OutboxEventProcessor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReserveFundsOutboxEventProcessor implements OutboxEventProcessor {
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
            outboxEventService.createOne(event.getId(), response, OutboxEventType.RESERVE_FUNDS);
            inboxEventReaderService.markOneAs(event, InboxEventStatus.COMPLETED);
        } catch (Exception ignored) {
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.RESERVE_FUNDS;
    }
}
