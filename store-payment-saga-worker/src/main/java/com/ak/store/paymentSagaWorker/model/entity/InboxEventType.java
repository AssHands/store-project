package com.ak.store.paymentSagaWorker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InboxEventType {
    RESERVE_FUNDS("RESERVE_FUNDS"),
    RELEASE_FUNDS("RELEASE_FUNDS");

    private String value;
}
