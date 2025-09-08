package com.ak.store.reviewSagaWorker.model.outbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  OutboxEventType {
    CONFIRM_REVIEW_CREATION("CONFIRM_REVIEW"),
    CANCEL_REVIEW_CREATION("CANCEL_REVIEW"),

    CONFIRM_REVIEW_UPDATE("CONFIRM_REVIEW_UPDATE"),
    CANCEL_REVIEW_UPDATE("CANCEL_REVIEW_UPDATE"),

    CONFIRM_REVIEW_DELETION("CONFIRM_REVIEW_DELETION"),
    CANCEL_REVIEW_DELETION("CANCEL_REVIEW_DELETION");

    private String value;
}
