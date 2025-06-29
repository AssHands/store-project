package com.ak.store.orderSagaWorker.processor.inbox;


import com.ak.store.orderSagaWorker.model.entity.InboxEvent;
import com.ak.store.orderSagaWorker.model.entity.InboxEventType;

public interface InboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}