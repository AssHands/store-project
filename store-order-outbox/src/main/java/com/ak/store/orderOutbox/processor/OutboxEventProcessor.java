package com.ak.store.orderOutbox.processor;

import com.ak.store.orderOutbox.model.OutboxEvent;
import com.ak.store.orderOutbox.model.OutboxEventType;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OutboxEventProcessor {
    void process(OutboxEvent event) throws JsonProcessingException;

    OutboxEventType getType();
}
