package com.ak.store.orderSagaWorker.processor.outbox;


import com.ak.store.orderSagaWorker.model.entity.InboxEvent;
import com.ak.store.orderSagaWorker.model.entity.InboxEventType;

public interface OutboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}
