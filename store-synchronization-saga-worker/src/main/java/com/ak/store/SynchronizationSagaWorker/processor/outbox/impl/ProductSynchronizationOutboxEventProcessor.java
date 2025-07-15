package com.ak.store.SynchronizationSagaWorker.processor.outbox.impl;

import com.ak.store.SynchronizationSagaWorker.model.entity.InboxEvent;
import com.ak.store.SynchronizationSagaWorker.model.entity.InboxEventStatus;
import com.ak.store.SynchronizationSagaWorker.model.entity.InboxEventType;
import com.ak.store.SynchronizationSagaWorker.model.entity.OutboxEventType;
import com.ak.store.SynchronizationSagaWorker.processor.outbox.OutboxEventProcessor;
import com.ak.store.SynchronizationSagaWorker.service.OutboxEventService;
import com.ak.store.common.saga.SagaResponseEvent;
import com.ak.store.common.saga.SagaResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductSynchronizationOutboxEventProcessor implements OutboxEventProcessor {
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
                .stepId(event.getId())
                .stepName(event.getStepName())
                .sagaId(event.getSagaId())
                .sagaName(event.getSagaName())
                .status(status)
                .build();

        outboxEventService.createOne(event.getId(), response, OutboxEventType.PRODUCT_SYNCHRONIZATION);
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.PRODUCT_SYNCHRONIZATION;
    }
}
