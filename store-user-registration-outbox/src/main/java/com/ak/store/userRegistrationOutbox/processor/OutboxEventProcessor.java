package com.ak.store.userRegistrationOutbox.processor;


import com.ak.store.userRegistrationOutbox.model.OutboxEvent;
import com.ak.store.userRegistrationOutbox.model.OutboxEventType;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OutboxEventProcessor {
    void process(OutboxEvent event) throws JsonProcessingException;

    OutboxEventType getType();
}
