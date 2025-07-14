package com.ak.store.userOutbox.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  OutboxEventType {
    USER_REGISTRATION("USER_REGISTRATION"),
    CANCEL_USER_REGISTRATION("CANCEL_USER_REGISTRATION"),
    USER_CREATION("USER_CREATION"),
    CONFIRM_USER_CREATION("CONFIRM_USER_CREATION"),
    CANCEL_USER_CREATION("CANCEL_USER_CREATION"),

    USER_VERIFICATION("USER_VERIFICATION");

    private final String value;
}
