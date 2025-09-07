package com.ak.store.orderSagaWorker.model.outbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  OutboxEventType {
    CONFIRM_ORDER("CONFIRM_ORDER"),
    CANCEL_ORDER("CANCEL_ORDER");

    private String value;
}
