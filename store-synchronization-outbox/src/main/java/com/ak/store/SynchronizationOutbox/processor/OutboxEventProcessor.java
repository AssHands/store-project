package com.ak.store.SynchronizationOutbox.processor;

import com.ak.store.SynchronizationOutbox.model.OutboxEvent;
import com.ak.store.SynchronizationOutbox.model.OutboxEventType;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OutboxEventProcessor {
    void process(OutboxEvent event);

    OutboxEventType getType();
}