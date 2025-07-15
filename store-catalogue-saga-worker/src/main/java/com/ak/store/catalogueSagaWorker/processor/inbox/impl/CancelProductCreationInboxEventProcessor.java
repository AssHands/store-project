package com.ak.store.catalogueSagaWorker.processor.inbox.impl;

import com.ak.store.catalogueSagaWorker.model.dto.CancelProductCreationRequest;
import com.ak.store.catalogueSagaWorker.model.entity.InboxEvent;
import com.ak.store.catalogueSagaWorker.model.entity.InboxEventType;
import com.ak.store.catalogueSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.catalogueSagaWorker.service.ProductService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelProductCreationInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final ProductService productService;

    @Override
    public void process(InboxEvent event) {
        var request = gson.fromJson(event.getPayload(), CancelProductCreationRequest.class);
        Long productId = request.getPayload().getProduct().getId();
        productService.deleteOne(productId);
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_PRODUCT_CREATION;
    }
}
