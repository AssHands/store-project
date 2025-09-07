package com.ak.store.sagaOrchestrator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SagaStepStatus {
    IN_PROGRESS("IN_PROGRESS"),
    EXPIRED("EXPIRED"),
    FAILED("FAILED"),
    COMPLETED("COMPLETED");

    private String value;
}