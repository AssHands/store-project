package com.ak.store.warehouseSagaWorker.processor.outbox;

import com.ak.store.warehouseSagaWorker.model.inbox.InboxEvent;
import com.ak.store.warehouseSagaWorker.model.inbox.InboxEventType;

public interface OutboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}
