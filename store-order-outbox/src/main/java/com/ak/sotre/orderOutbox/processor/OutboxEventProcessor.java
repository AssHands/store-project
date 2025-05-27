package com.ak.sotre.orderOutbox.processor;

import com.ak.sotre.orderOutbox.model.OutboxEvent;
import com.ak.sotre.orderOutbox.model.OutboxEventType;

public interface OutboxEventProcessor {
    void process(OutboxEvent task);

    OutboxEventType getType();
}
