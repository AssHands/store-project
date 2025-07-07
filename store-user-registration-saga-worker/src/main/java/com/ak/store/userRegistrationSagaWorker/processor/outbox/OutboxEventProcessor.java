package com.ak.store.userRegistrationSagaWorker.processor.outbox;

import com.ak.store.userRegistrationSagaWorker.model.entity.InboxEvent;
import com.ak.store.userRegistrationSagaWorker.model.entity.InboxEventType;

public interface OutboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}
