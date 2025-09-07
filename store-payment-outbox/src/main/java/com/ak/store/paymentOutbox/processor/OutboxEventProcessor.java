package com.ak.store.paymentOutbox.processor;

import com.ak.store.paymentOutbox.model.OutboxEvent;
import com.ak.store.paymentOutbox.model.OutboxEventType;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface OutboxEventProcessor {
    void process(OutboxEvent event);

    OutboxEventType getType();
}