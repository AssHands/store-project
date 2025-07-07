package com.ak.store.userRegistration.outbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutboxEventType {
    USER_REGISTRATION("USER_REGISTRATION");

    private String value;
}
