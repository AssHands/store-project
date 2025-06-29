package com.ak.store.warehouseOutbox.processor;

import com.ak.store.warehouseOutbox.model.OutboxEvent;
import com.ak.store.warehouseOutbox.model.OutboxEventType;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OutboxEventProcessor {
    void process(OutboxEvent event) throws JsonProcessingException;

    OutboxEventType getType();
}
