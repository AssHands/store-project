package com.ak.store.emailSender.inbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InboxEventType {
    ORDER_CREATED("ORDER_CREATED"),
    USER_VERIFY("USER_VERIFY");

    private String type;
}
