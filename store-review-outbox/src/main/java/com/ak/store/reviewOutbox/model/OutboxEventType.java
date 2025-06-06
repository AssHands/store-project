package com.ak.store.reviewOutbox.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutboxEventType {
    REVIEW_CREATED("REVIEW_CREATED"),
    REVIEW_UPDATED("REVIEW_UPDATED"),
    REVIEW_DELETED("REVIEW_DELETED");

    private String type;
}