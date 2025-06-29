package com.ak.store.orderSagaWorker.processor.outbox.impl;

import com.ak.store.common.saga.SagaResponseEvent;
import com.ak.store.common.saga.SagaResponseStatus;
import com.ak.store.orderSagaWorker.model.entity.InboxEvent;
import com.ak.store.orderSagaWorker.model.entity.InboxEventStatus;
import com.ak.store.orderSagaWorker.model.entity.InboxEventType;
import com.ak.store.orderSagaWorker.model.entity.OutboxEventType;
import com.ak.store.orderSagaWorker.processor.outbox.OutboxEventProcessor;
import com.ak.store.orderSagaWorker.service.OutboxEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelOrderOutboxEventProcessor implements OutboxEventProcessor {
    private final OutboxEventService outboxEventService;

    @Override
    public void process(InboxEvent event) {
        SagaResponseStatus status;

        if (event.getStatus() == InboxEventStatus.SUCCESS) {
            status = SagaResponseStatus.SUCCESS;
        } else {
            status = SagaResponseStatus.FAILURE;
        }

        var response = SagaResponseEvent.builder()
                .status(status)
                .sagaId(event.getId())
                .stepName(event.getStepName())
                .build();

        outboxEventService.createOne(event.getId(), response, OutboxEventType.CANCEL_ORDER);
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_ORDER;
    }
}