package com.ak.store.catalogueSagaWorker.processor.inbox.impl;

import com.ak.store.catalogueSagaWorker.model.dto.ConfirmProductCreationRequest;
import com.ak.store.catalogueSagaWorker.model.entity.InboxEvent;
import com.ak.store.catalogueSagaWorker.model.entity.InboxEventType;
import com.ak.store.catalogueSagaWorker.model.entity.ProductStatus;
import com.ak.store.catalogueSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.catalogueSagaWorker.service.ProductService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConfirmProductCreationInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final ProductService productService;

    @Override
    public void process(InboxEvent event) {
        var request = gson.fromJson(event.getPayload(), ConfirmProductCreationRequest.class);
        productService.setStatus(request.getPayload().getProduct().getId(), ProductStatus.ACTIVE);
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CONFIRM_PRODUCT_CREATION;
    }
}
