package com.ak.store.warehouseSagaWorker.processor.inbox.impl;

import com.ak.store.warehouseSagaWorker.model.dto.CancelInventoryCreationRequest;
import com.ak.store.warehouseSagaWorker.model.inbox.InboxEvent;
import com.ak.store.warehouseSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.warehouseSagaWorker.model.inbox.InboxEventType;
import com.ak.store.warehouseSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.warehouseSagaWorker.service.InboxEventReaderService;
import com.ak.store.warehouseSagaWorker.service.InventoryService;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelInventoryCreationInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final InventoryService inventoryService;
    private final InboxEventReaderService inboxEventReaderService;

    @Transactional
    @Override
    public void process(InboxEvent event) {
        var request = gson.fromJson(event.getPayload(), CancelInventoryCreationRequest.class);

        try {
            inventoryService.deleteOne(request.getPayload().getProduct().getId());
            inboxEventReaderService.markOneAs(event, InboxEventStatus.SUCCESS);
        } catch (Exception e) {
            inboxEventReaderService.markOneAsFailure(event);
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_INVENTORY_CREATION;
    }
}
