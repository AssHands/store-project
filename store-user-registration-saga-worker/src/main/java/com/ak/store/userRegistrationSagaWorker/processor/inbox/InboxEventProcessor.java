package com.ak.store.userRegistrationSagaWorker.processor.inbox;


import com.ak.store.userRegistrationSagaWorker.model.entity.InboxEvent;
import com.ak.store.userRegistrationSagaWorker.model.entity.InboxEventType;

public interface InboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}