package com.ak.store.reviewOutbox.processor;

import com.ak.store.reviewOutbox.model.OutboxEvent;
import com.ak.store.reviewOutbox.model.OutboxEventType;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OutboxEventProcessor {
    void process(OutboxEvent event) throws JsonProcessingException;

    OutboxEventType getType();
}