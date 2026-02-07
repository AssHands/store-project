package com.ak.store.SynchronizationSagaWorker.processor.inbox.impl;

import com.ak.store.SynchronizationSagaWorker.model.inbox.InboxEvent;
import com.ak.store.SynchronizationSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.SynchronizationSagaWorker.model.inbox.InboxEventType;
import com.ak.store.SynchronizationSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.SynchronizationSagaWorker.service.InboxEventReaderService;
import com.ak.store.SynchronizationSagaWorker.service.ProductService;
import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.product.ProductSnapshotPayload;
import com.ak.store.kafka.storekafkastarter.util.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.event.catalogue.product.ProductCreatedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelProductSynchronizationInboxEventProcessor implements InboxEventProcessor {
    private final JsonMapperKafka jsonMapperKafka;
    private final InboxEventReaderService inboxEventReaderService;
    private final ProductService productService;

    @Transactional
    @Override
    public void process(InboxEvent event) {
        var snapshot = jsonMapperKafka.fromJson(event.getPayload(), ProductSnapshotPayload.class);

        try {
            productService.deleteOne(snapshot.getProduct().getId());
            inboxEventReaderService.markOneAs(event, InboxEventStatus.SUCCESS);
        } catch (Exception e) {
            inboxEventReaderService.markOneAsFailure(event);
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_PRODUCT_SYNCHRONIZATION;
    }
}
