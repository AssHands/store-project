package com.ak.store.catalogueSagaWorker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutboxEventStatus {
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED");

    private String status;
}
