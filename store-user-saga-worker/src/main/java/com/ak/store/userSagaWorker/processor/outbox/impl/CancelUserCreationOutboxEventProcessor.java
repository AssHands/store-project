package com.ak.store.userSagaWorker.processor.outbox.impl;

import com.ak.store.common.saga.SagaResponseEvent;
import com.ak.store.common.saga.SagaResponseStatus;
import com.ak.store.userSagaWorker.model.entity.InboxEvent;
import com.ak.store.userSagaWorker.model.entity.InboxEventStatus;
import com.ak.store.userSagaWorker.model.entity.InboxEventType;
import com.ak.store.userSagaWorker.model.entity.OutboxEventType;
import com.ak.store.userSagaWorker.processor.outbox.OutboxEventProcessor;
import com.ak.store.userSagaWorker.service.OutboxEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelUserCreationOutboxEventProcessor implements OutboxEventProcessor {
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
                .sagaId(event.getSagaId())
                .stepName(event.getStepName())
                .build();

        outboxEventService.createOne(event.getId(), response, OutboxEventType.CANCEL_USER_CREATION);
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_USER_CREATION;
    }
}

