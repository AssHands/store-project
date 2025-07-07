package com.ak.store.userRegistrationSagaWorker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  OutboxEventType {
    CANCEL_USER_REGISTRATION("CANCEL_USER_REGISTRATION");

    private String value;
}
