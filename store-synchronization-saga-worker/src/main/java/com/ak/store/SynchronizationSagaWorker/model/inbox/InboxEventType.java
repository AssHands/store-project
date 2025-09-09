package com.ak.store.SynchronizationSagaWorker.model.inbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InboxEventType {
    PRODUCT_SYNCHRONIZATION("PRODUCT_SYNCHRONIZATION"),
    CANCEL_PRODUCT_SYNCHRONIZATION("CANCEL_PRODUCT_SYNCHRONIZATION");

    private final String value;
}
