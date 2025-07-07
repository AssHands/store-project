package com.ak.store.userSagaWorker.processor.inbox;


import com.ak.store.userSagaWorker.model.entity.InboxEvent;
import com.ak.store.userSagaWorker.model.entity.InboxEventType;

public interface InboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}