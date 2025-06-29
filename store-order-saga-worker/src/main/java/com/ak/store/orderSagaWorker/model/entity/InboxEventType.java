package com.ak.store.orderSagaWorker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InboxEventType {
    CONFIRM_ORDER("CONFIRM_ORDER"),
    CANCEL_ORDER("CANCEL_ORDER");

    private String value;
}
