package com.ak.store.userSagaWorker.processor.inbox;


import com.ak.store.userSagaWorker.model.inbox.InboxEvent;
import com.ak.store.userSagaWorker.model.inbox.InboxEventType;

public interface InboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}