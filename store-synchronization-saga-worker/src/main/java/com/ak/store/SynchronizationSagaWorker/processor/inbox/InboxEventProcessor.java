package com.ak.store.SynchronizationSagaWorker.processor.inbox;


import com.ak.store.SynchronizationSagaWorker.model.inbox.InboxEvent;
import com.ak.store.SynchronizationSagaWorker.model.inbox.InboxEventType;

public interface InboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}