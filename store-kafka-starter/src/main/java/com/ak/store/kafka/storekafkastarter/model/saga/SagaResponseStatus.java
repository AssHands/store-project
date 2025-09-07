package com.ak.store.kafka.storekafkastarter.model.saga;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SagaResponseStatus {
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE");

    private final String value;
}
