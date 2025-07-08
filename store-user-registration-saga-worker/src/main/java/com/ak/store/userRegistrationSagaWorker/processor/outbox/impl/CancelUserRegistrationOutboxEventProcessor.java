package com.ak.store.userRegistrationSagaWorker.processor.outbox.impl;

import com.ak.store.common.saga.SagaResponseEvent;
import com.ak.store.common.saga.SagaResponseStatus;
import com.ak.store.userRegistrationSagaWorker.model.entity.InboxEvent;
import com.ak.store.userRegistrationSagaWorker.model.entity.InboxEventStatus;
import com.ak.store.userRegistrationSagaWorker.model.entity.InboxEventType;
import com.ak.store.userRegistrationSagaWorker.model.entity.OutboxEventType;
import com.ak.store.userRegistrationSagaWorker.processor.outbox.OutboxEventProcessor;
import com.ak.store.userRegistrationSagaWorker.service.OutboxEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelUserRegistrationOutboxEventProcessor implements OutboxEventProcessor {
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

        outboxEventService.createOne(event.getId(), response, OutboxEventType.CANCEL_USER_REGISTRATION);
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_USER_REGISTRATION;
    }
}

