package com.ak.store.userOutbox.processor;

import com.ak.store.userOutbox.model.OutboxEvent;
import com.ak.store.userOutbox.model.OutboxEventType;

public interface OutboxEventProcessor {
    void process(OutboxEvent event);

    OutboxEventType getType();
}
