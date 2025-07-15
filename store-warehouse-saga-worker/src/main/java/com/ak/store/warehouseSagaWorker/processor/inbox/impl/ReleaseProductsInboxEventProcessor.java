package com.ak.store.warehouseSagaWorker.processor.inbox.impl;

import com.ak.store.warehouseSagaWorker.model.dto.ReleaseProductsRequest;
import com.ak.store.warehouseSagaWorker.model.entity.InboxEvent;
import com.ak.store.warehouseSagaWorker.model.entity.InboxEventType;
import com.ak.store.warehouseSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.warehouseSagaWorker.service.InventoryService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReleaseProductsInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final InventoryService inventoryService;

    @Override
    public void process(InboxEvent event) {
        var request = gson.fromJson(event.getPayload(), ReleaseProductsRequest.class);
        inventoryService.releaseAll(request.getProductAmount());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.RELEASE_PRODUCTS;
    }
}
