package com.ak.store.catalogueSagaWorker.processor.outbox.impl;

import com.ak.store.catalogueSagaWorker.model.entity.InboxEvent;
import com.ak.store.catalogueSagaWorker.model.entity.InboxEventStatus;
import com.ak.store.catalogueSagaWorker.model.entity.InboxEventType;
import com.ak.store.catalogueSagaWorker.model.entity.OutboxEventType;
import com.ak.store.catalogueSagaWorker.processor.outbox.OutboxEventProcessor;
import com.ak.store.catalogueSagaWorker.service.OutboxEventService;
import com.ak.store.common.saga.SagaResponseEvent;
import com.ak.store.common.saga.SagaResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelDeleteProductGradeOutboxEventProcessor implements OutboxEventProcessor {
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

        outboxEventService.createOne(event.getId(), response, OutboxEventType.CANCEL_DELETE_PRODUCT_GRADE);
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_DELETE_PRODUCT_GRADE;
    }
}
