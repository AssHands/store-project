package com.ak.store.warehouseSagaWorker.processor.outbox.impl;

import com.ak.store.kafka.storekafkastarter.model.saga.SagaResponseEvent;
import com.ak.store.kafka.storekafkastarter.model.saga.SagaResponseStatus;
import com.ak.store.warehouseSagaWorker.model.inbox.InboxEvent;
import com.ak.store.warehouseSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.warehouseSagaWorker.model.inbox.InboxEventType;
import com.ak.store.warehouseSagaWorker.model.outbox.OutboxEventType;
import com.ak.store.warehouseSagaWorker.processor.outbox.OutboxEventProcessor;
import com.ak.store.warehouseSagaWorker.service.InboxEventReaderService;
import com.ak.store.warehouseSagaWorker.service.OutboxEventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReleaseProductsOutboxEventProcessor implements OutboxEventProcessor {
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
            outboxEventService.createOne(event.getId(), response, OutboxEventType.RELEASE_PRODUCTS);
            inboxEventReaderService.markOneAs(event, InboxEventStatus.COMPLETED);
        } catch (Exception ignored) {
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.RELEASE_PRODUCTS;
    }
}

