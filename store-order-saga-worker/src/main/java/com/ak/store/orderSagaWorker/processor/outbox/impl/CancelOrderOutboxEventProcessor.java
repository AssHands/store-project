package com.ak.store.orderSagaWorker.processor.outbox.impl;


import com.ak.store.kafka.storekafkastarter.model.saga.SagaResponseEvent;
import com.ak.store.kafka.storekafkastarter.model.saga.SagaResponseStatus;
import com.ak.store.orderSagaWorker.model.inbox.InboxEvent;
import com.ak.store.orderSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.orderSagaWorker.model.inbox.InboxEventType;
import com.ak.store.orderSagaWorker.model.outbox.OutboxEventType;
import com.ak.store.orderSagaWorker.processor.outbox.OutboxEventProcessor;
import com.ak.store.orderSagaWorker.service.InboxEventReaderService;
import com.ak.store.orderSagaWorker.service.OutboxEventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelOrderOutboxEventProcessor implements OutboxEventProcessor {
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
            outboxEventService.createOne(event.getId(), response, OutboxEventType.CANCEL_ORDER);
            inboxEventReaderService.markOneAs(event, InboxEventStatus.COMPLETED);
        } catch (Exception ignored) {
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_ORDER;
    }
}