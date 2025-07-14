package com.ak.store.emailSender.inbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InboxEventType {
    USER_VERIFICATION("USER_VERIFICATION");

    private final String value;
}