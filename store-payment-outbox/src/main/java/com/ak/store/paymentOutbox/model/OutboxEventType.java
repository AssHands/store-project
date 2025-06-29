package com.ak.store.paymentOutbox.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutboxEventType {
    RESERVE_FUNDS("RESERVE_FUNDS"),
    RELEASE_FUNDS("RELEASE_FUNDS");

    private String value;
}