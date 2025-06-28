package com.ak.store.paymentSagaWorker.inbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InboxEventStatus {
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED");

    private String status;
}
