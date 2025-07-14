package com.ak.store.user.outbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  OutboxEventType {
    USER_REGISTRATION("USER_REGISTRATION"),
    USER_VERIFICATION("USER_VERIFICATION");

    private final String value;
}
