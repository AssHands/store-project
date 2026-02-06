package com.ak.store.warehouseSagaWorker.processor.inbox.impl;

import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.snapshot.order.OrderCreatedSnapshot;
import com.ak.store.warehouseSagaWorker.model.inbox.InboxEvent;
import com.ak.store.warehouseSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.warehouseSagaWorker.model.inbox.InboxEventType;
import com.ak.store.warehouseSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.warehouseSagaWorker.service.InboxEventReaderService;
import com.ak.store.warehouseSagaWorker.service.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReleaseProductsInboxEventProcessor implements InboxEventProcessor {
    private final JsonMapperKafka jsonMapperKafka;
    private final InventoryService inventoryService;
    private final InboxEventReaderService inboxEventReaderService;

    @Transactional
    @Override
    public void process(InboxEvent event) {
        var request = jsonMapperKafka.fromJson(event.getPayload(), OrderCreatedSnapshot.class);

        try {
            inventoryService.releaseAll(request.getProductAmount());
            inboxEventReaderService.markOneAs(event, InboxEventStatus.SUCCESS);
        } catch (Exception e) {
            inboxEventReaderService.markOneAsFailure(event);
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.RELEASE_PRODUCTS;
    }
}
