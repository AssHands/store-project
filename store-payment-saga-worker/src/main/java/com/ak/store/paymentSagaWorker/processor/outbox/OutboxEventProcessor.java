package com.ak.store.paymentSagaWorker.processor.outbox;

import com.ak.store.paymentSagaWorker.model.entity.InboxEvent;
import com.ak.store.paymentSagaWorker.model.entity.InboxEventType;

public interface OutboxEventProcessor {
    void process(InboxEvent event);

    InboxEventType getType();
}
