package com.ak.store.outboxScheduler.processor;

import com.ak.store.outboxScheduler.model.OutboxTask;
import com.ak.store.outboxScheduler.model.OutboxTaskType;

import java.util.List;

public interface OutboxTaskProcessor {
    void process(OutboxTask task);

    OutboxTaskType getType();
}
