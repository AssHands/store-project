package com.ak.store.orderSagaWorker.processor.inbox;


import com.ak.store.orderSagaWorker.model.inbox.InboxEvent;
import com.ak.store.orderSagaWorker.model.inbox.InboxEventType;

public interface InboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}