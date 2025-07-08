package com.ak.store.paymentSagaWorker.processor.outbox.impl;

import com.ak.store.common.saga.SagaResponseEvent;
import com.ak.store.common.saga.SagaResponseStatus;
import com.ak.store.paymentSagaWorker.model.entity.InboxEvent;
import com.ak.store.paymentSagaWorker.model.entity.InboxEventStatus;
import com.ak.store.paymentSagaWorker.model.entity.InboxEventType;
import com.ak.store.paymentSagaWorker.model.entity.OutboxEventType;
import com.ak.store.paymentSagaWorker.processor.outbox.OutboxEventProcessor;
import com.ak.store.paymentSagaWorker.service.OutboxEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserPaymentCreationOutboxEventProcessor implements OutboxEventProcessor {
    private final OutboxEventService outboxEventService;

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

        outboxEventService.createOne(event.getId(), response, OutboxEventType.USER_PAYMENT_CREATION);
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.USER_PAYMENT_CREATION;
    }
}
