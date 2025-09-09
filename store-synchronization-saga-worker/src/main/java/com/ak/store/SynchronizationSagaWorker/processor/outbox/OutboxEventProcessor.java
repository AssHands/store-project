package com.ak.store.SynchronizationSagaWorker.processor.outbox;


import com.ak.store.SynchronizationSagaWorker.model.inbox.InboxEvent;
import com.ak.store.SynchronizationSagaWorker.model.inbox.InboxEventType;

public interface OutboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}
