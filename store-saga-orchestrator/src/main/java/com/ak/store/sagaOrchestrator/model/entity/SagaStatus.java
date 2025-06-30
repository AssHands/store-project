package com.ak.store.sagaOrchestrator.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SagaStatus {
    RECEIVED("RECEIVED"),
    IN_PROGRESS("IN_PROGRESS"),
    FAILED("FAILED"),
    COMPLETED("COMPLETED");

    private String value;
}