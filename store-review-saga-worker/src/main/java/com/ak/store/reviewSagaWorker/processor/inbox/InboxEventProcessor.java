package com.ak.store.reviewSagaWorker.processor.inbox;


import com.ak.store.reviewSagaWorker.model.entity.InboxEvent;
import com.ak.store.reviewSagaWorker.model.entity.InboxEventType;

public interface InboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}