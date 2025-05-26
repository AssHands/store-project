package com.ak.store.emailSender.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailEventType {
    ORDER_CREATED("ORDER_CREATED"),
    USER_VERIFY("USER_VERIFY");

    private String type;
}
