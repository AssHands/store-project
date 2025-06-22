package com.ak.store.common.saga;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SagaResponseStatus {
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE");

    private String value;
}