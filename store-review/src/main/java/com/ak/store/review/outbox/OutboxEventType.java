package com.ak.store.review.outbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutboxEventType {
    REVIEW_CREATION("REVIEW_CREATED"),
    REVIEW_UPDATE("REVIEW_UPDATE"),
    REVIEW_DELETION("REVIEW_DELETION");

    private final String value;
}