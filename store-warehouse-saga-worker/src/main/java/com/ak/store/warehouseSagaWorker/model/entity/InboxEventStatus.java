package com.ak.store.warehouseSagaWorker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InboxEventStatus {
    IN_PROGRESS("IN_PROGRESS"),
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE"),
    COMPLETED("COMPLETED");

    private String value;
}
