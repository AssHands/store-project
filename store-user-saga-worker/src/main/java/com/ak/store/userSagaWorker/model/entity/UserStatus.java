package com.ak.store.userSagaWorker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {
    PENDING_REGISTRATION("PENDING_REGISTRATION"),
    PENDING_EMAIL_UPDATING("PENDING_EMAIL_UPDATING"),
    ACTIVE("ACTIVE"),
    BANNED("BANNED");


    private String value;
}