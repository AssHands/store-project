package com.ak.store.reviewSagaWorker.model.outbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  OutboxEventType {
    CONFIRM_REVIEW_CREATED("CONFIRM_REVIEW_CREATED"),
    CANCEL_REVIEW_CREATED("CANCEL_REVIEW_CREATED"),

    CONFIRM_REVIEW_UPDATED("CONFIRM_REVIEW_UPDATED"),
    CANCEL_REVIEW_UPDATED("CANCEL_REVIEW_UPDATED"),

    CONFIRM_REVIEW_DELETED("CONFIRM_REVIEW_DELETED"),
    CANCEL_REVIEW_DELETED("CANCEL_REVIEW_DELETED");

    private final String value;
}
