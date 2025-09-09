package com.ak.store.SynchronizationSagaWorker.model.outbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  OutboxEventType {
    PRODUCT_SYNCHRONIZATION("PRODUCT_SYNCHRONIZATION"),
    CANCEL_PRODUCT_SYNCHRONIZATION("CANCEL_PRODUCT_SYNCHRONIZATION");

    private final String value;
}
