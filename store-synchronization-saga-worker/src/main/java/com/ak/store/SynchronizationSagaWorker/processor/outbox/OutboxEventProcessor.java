package com.ak.store.SynchronizationSagaWorker.processor.outbox;


import com.ak.store.SynchronizationSagaWorker.model.entity.InboxEvent;
import com.ak.store.SynchronizationSagaWorker.model.entity.InboxEventType;

public interface OutboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}
