package com.ak.store.userSagaWorker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InboxEventType {
    CANCEL_USER_REGISTRATION("CANCEL_USER_REGISTRATION"),
    USER_CREATION("USER_CREATION"),
    CANCEL_USER_CREATION("CANCEL_USER_CREATION"),
    CONFIRM_USER_CREATION("CONFIRM_USER_CREATION");

    private final String value;
}
