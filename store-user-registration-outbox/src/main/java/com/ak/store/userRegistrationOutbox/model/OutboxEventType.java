package com.ak.store.userRegistrationOutbox.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutboxEventType {
    USER_REGISTRATION("USER_REGISTRATION"),
    CANCEL_USER_REGISTRATION("CANCEL_USER_REGISTRATION");

    private String value;
}
