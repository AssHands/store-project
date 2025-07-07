package com.ak.store.userSagaWorker.processor.outbox;

import com.ak.store.userSagaWorker.model.entity.InboxEvent;
import com.ak.store.userSagaWorker.model.entity.InboxEventType;

public interface OutboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}
