package com.ak.store.reviewSagaWorker.processor.outbox;


import com.ak.store.reviewSagaWorker.model.entity.InboxEvent;
import com.ak.store.reviewSagaWorker.model.entity.InboxEventType;

public interface OutboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}
