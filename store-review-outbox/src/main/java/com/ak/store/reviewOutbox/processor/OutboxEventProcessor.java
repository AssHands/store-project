package com.ak.store.reviewOutbox.processor;

import com.ak.store.reviewOutbox.model.OutboxEvent;
import com.ak.store.reviewOutbox.model.OutboxEventType;

public interface OutboxEventProcessor {
    void process(OutboxEvent event);

    OutboxEventType getType();
}