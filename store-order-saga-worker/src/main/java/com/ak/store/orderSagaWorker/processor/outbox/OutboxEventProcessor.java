package com.ak.store.orderSagaWorker.processor.outbox;


import com.ak.store.orderSagaWorker.model.inbox.InboxEvent;
import com.ak.store.orderSagaWorker.model.inbox.InboxEventType;

public interface OutboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}
