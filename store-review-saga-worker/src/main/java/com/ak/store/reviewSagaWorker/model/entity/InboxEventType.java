package com.ak.store.reviewSagaWorker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InboxEventType {
    CONFIRM_REVIEW_CREATION("CONFIRM_REVIEW_CREATION"),
    CANCEL_REVIEW_CREATION("CANCEL_REVIEW_CREATION"),

    CONFIRM_REVIEW_UPDATE("CONFIRM_REVIEW_UPDATE"),
    CANCEL_REVIEW_UPDATE("CANCEL_REVIEW_UPDATE"),

    CONFIRM_REVIEW_DELETION("CONFIRM_REVIEW_DELETION"),
    CANCEL_REVIEW_DELETION("CANCEL_REVIEW_DELETION");

    private final String value;
}
