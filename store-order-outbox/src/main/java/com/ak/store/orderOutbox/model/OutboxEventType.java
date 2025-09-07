package com.ak.store.orderOutbox.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutboxEventType {
    ORDER_CREATION("ORDER_CREATION"),
    CONFIRM_ORDER("CONFIRM_ORDER"),
    CANCEL_ORDER("CANCEL_ORDER");

    private final String value;
}