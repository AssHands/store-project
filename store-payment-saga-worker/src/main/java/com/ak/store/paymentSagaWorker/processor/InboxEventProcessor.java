package com.ak.store.paymentSagaWorker.processor;

import com.ak.store.paymentSagaWorker.inbox.InboxEvent;
import com.ak.store.paymentSagaWorker.inbox.InboxEventType;

public interface InboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}