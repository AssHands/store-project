package com.ak.store.catalogueSagaWorker.processor.inbox;


import com.ak.store.catalogueSagaWorker.model.entity.InboxEvent;
import com.ak.store.catalogueSagaWorker.model.entity.InboxEventType;

public interface InboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}