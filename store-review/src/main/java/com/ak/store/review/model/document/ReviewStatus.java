package com.ak.store.review.model.document;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReviewStatus {
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED");

    private final String value;
}