package com.ak.store.SynchronizationSagaWorker.processor.inbox.impl;

import com.ak.store.SynchronizationSagaWorker.model.dto.ProductSynchronizationRequest;
import com.ak.store.SynchronizationSagaWorker.model.entity.InboxEvent;
import com.ak.store.SynchronizationSagaWorker.model.entity.InboxEventType;
import com.ak.store.SynchronizationSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.SynchronizationSagaWorker.service.ProductService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelProductSynchronizationInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final ProductService productService;

    @Override
    public void process(InboxEvent event) {
        var request = gson.fromJson(event.getPayload(), ProductSynchronizationRequest.class);
        productService.deleteOne(request.getPayload().getProduct().getId());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_PRODUCT_SYNCHRONIZATION;
    }
}
