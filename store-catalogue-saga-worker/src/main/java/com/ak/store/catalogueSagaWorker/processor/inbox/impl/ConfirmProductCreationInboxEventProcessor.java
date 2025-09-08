package com.ak.store.catalogueSagaWorker.processor.inbox.impl;

import com.ak.store.catalogueSagaWorker.model.inbox.InboxEvent;
import com.ak.store.catalogueSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.catalogueSagaWorker.model.inbox.InboxEventType;
import com.ak.store.catalogueSagaWorker.model.product.ProductStatus;
import com.ak.store.catalogueSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.catalogueSagaWorker.service.InboxEventReaderService;
import com.ak.store.catalogueSagaWorker.service.ProductService;
import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.ProductCreationSnapshot;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConfirmProductCreationInboxEventProcessor implements InboxEventProcessor {
    private final JsonMapperKafka jsonMapperKafka;
    private final ProductService productService;
    private final InboxEventReaderService inboxEventReaderService;

    @Transactional
    @Override
    public void process(InboxEvent event) {
        var request = jsonMapperKafka.fromJson(event.getPayload(), ProductCreationSnapshot.class);

        try {
            productService.setStatus(request.getPayload().getProduct().getId(), ProductStatus.ACTIVE);
            inboxEventReaderService.markOneAs(event, InboxEventStatus.SUCCESS);
        } catch (Exception e) {
            inboxEventReaderService.markOneAsFailure(event);
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CONFIRM_PRODUCT_CREATION;
    }
}
