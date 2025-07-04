package com.ak.store.user.outbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  OutboxEventType {
    VERIFY_USER("VERIFY_USER"),
    USER_CREATED("USER_CREATED");

    private String value;
}
