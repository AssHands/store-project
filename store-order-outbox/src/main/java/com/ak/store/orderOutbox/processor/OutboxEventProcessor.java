package com.ak.store.orderOutbox.processor;

import com.ak.store.orderOutbox.model.OutboxEvent;
import com.ak.store.orderOutbox.model.OutboxEventType;

public interface OutboxEventProcessor {
    void process(OutboxEvent event);

    OutboxEventType getType();
}
