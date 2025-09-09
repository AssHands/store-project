package com.ak.store.userSagaWorker.model.outbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  OutboxEventType {
    CANCEL_USER_REGISTRATION("CANCEL_USER_REGISTRATION"),
    USER_CREATION("USER_CREATION"),
    CANCEL_USER_CREATION("CANCEL_USER_CREATION"),
    CONFIRM_USER_CREATION("CONFIRM_USER_CREATION"),

    USER_VERIFICATION("USER_VERIFICATION");

    private final String value;
}