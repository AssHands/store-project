package com.ak.store.paymentSagaWorker.processor.inbox;

import com.ak.store.paymentSagaWorker.model.inbox.InboxEvent;
import com.ak.store.paymentSagaWorker.model.inbox.InboxEventType;

public interface InboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}