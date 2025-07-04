package com.ak.store.userOutbox.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  OutboxEventType {
    VERIFY_USER("VERIFY_USER"),
    USER_CREATED("USER_CREATED");

    private String value;
}
