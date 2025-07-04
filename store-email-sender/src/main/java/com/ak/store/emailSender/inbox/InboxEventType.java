package com.ak.store.emailSender.inbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InboxEventType {
    USER_CREATED("USER_CREATED"),
    VERIFY_USER("VERIFY_USER");

    private String value;
}
