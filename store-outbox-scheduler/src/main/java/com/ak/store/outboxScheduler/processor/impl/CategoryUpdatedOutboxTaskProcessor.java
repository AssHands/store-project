package com.ak.store.outboxScheduler.processor.impl;

import com.ak.store.outboxScheduler.model.OutboxTask;
import com.ak.store.outboxScheduler.model.OutboxTaskType;
import com.ak.store.outboxScheduler.processor.OutboxTaskProcessor;

import java.util.List;

public class CategoryUpdatedOutboxTaskProcessor implements OutboxTaskProcessor {
    @Override
    public void process(List<OutboxTask> tasks) {

    }

    @Override
    public OutboxTaskType getType() {
        return OutboxTaskType.CATEGORY_UPDATED;
    }
}
