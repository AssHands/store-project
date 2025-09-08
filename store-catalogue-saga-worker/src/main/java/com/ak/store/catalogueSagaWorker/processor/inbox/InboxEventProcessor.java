package com.ak.store.catalogueSagaWorker.processor.inbox;


import com.ak.store.catalogueSagaWorker.model.inbox.InboxEvent;
import com.ak.store.catalogueSagaWorker.model.inbox.InboxEventType;

public interface InboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}