package com.ak.store.reviewSagaWorker.model.inbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InboxEventType {
    CONFIRM_REVIEW_CREATED("CONFIRM_REVIEW_CREATED"),
    CANCEL_REVIEW_CREATED("CANCEL_REVIEW_CREATED"),

    CONFIRM_REVIEW_UPDATED("CONFIRM_REVIEW_UPDATED"),
    CANCEL_REVIEW_UPDATED("CANCEL_REVIEW_UPDATED"),

    CONFIRM_REVIEW_DELETED("CONFIRM_REVIEW_DELETED"),
    CANCEL_REVIEW_DELETED("CANCEL_REVIEW_DELETED");

    private final String value;
}
