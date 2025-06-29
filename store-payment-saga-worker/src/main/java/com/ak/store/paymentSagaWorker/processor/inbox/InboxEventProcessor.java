package com.ak.store.paymentSagaWorker.processor.inbox;

import com.ak.store.paymentSagaWorker.model.entity.InboxEvent;
import com.ak.store.paymentSagaWorker.model.entity.InboxEventType;

public interface InboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}