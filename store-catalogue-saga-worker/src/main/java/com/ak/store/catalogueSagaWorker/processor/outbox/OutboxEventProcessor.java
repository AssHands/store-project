package com.ak.store.catalogueSagaWorker.processor.outbox;


import com.ak.store.catalogueSagaWorker.model.entity.InboxEvent;
import com.ak.store.catalogueSagaWorker.model.entity.InboxEventType;

public interface OutboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}
