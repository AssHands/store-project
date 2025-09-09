package com.ak.store.userSagaWorker.processor.outbox;

import com.ak.store.userSagaWorker.model.inbox.InboxEvent;
import com.ak.store.userSagaWorker.model.inbox.InboxEventType;

public interface OutboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}
