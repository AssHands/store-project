package com.ak.store.catalogueSagaWorker.processor.inbox.impl;

import com.ak.store.catalogueSagaWorker.model.inbox.InboxEvent;
import com.ak.store.catalogueSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.catalogueSagaWorker.model.inbox.InboxEventType;
import com.ak.store.catalogueSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.catalogueSagaWorker.service.InboxEventReaderService;
import com.ak.store.catalogueSagaWorker.service.ProductService;
import com.ak.store.kafka.storekafkastarter.util.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.product.ProductSnapshotPayload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelProductCreatedInboxEventProcessor implements InboxEventProcessor {
    private final JsonMapperKafka jsonMapperKafka;
    private final ProductService productService;
    private final InboxEventReaderService inboxEventReaderService;

    @Transactional
    @Override
    public void process(InboxEvent event) {
        var request = jsonMapperKafka.fromJson(event.getPayload(), ProductSnapshotPayload.class);

        try {
            productService.deleteOne(request.getProduct().getId());
            inboxEventReaderService.markOneAs(event, InboxEventStatus.SUCCESS);
        } catch (Exception e) {
            inboxEventReaderService.markOneAsFailure(event);
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_PRODUCT_CREATED;
    }
}
