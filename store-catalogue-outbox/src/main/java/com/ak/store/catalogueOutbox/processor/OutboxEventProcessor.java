package com.ak.store.catalogueOutbox.processor;

import com.ak.store.catalogueOutbox.model.OutboxEvent;
import com.ak.store.catalogueOutbox.model.OutboxEventType;

public interface OutboxEventProcessor {
    void process(OutboxEvent event);

    OutboxEventType getType();
}
