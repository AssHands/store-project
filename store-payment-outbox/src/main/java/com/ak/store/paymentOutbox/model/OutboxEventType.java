package com.ak.store.paymentOutbox.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutboxEventType {
    RESERVE_FUNDS("RESERVE_FUNDS"),
    RELEASE_FUNDS("RELEASE_FUNDS"),
    USER_PAYMENT_CREATION("USER_PAYMENT_CREATION"),
    CANCEL_USER_PAYMENT_CREATION("CANCEL_USER_PAYMENT_CREATION");

    private final String value;
}