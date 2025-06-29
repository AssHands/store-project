package com.ak.store.warehouseSagaWorker.processor.outbox;

import com.ak.store.warehouseSagaWorker.model.entity.InboxEvent;
import com.ak.store.warehouseSagaWorker.model.entity.InboxEventType;

public interface OutboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}
