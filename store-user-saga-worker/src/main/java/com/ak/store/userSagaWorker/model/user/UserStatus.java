package com.ak.store.userSagaWorker.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {
    PENDING_REGISTRATION("PENDING_REGISTRATION"),
    PENDING_VERIFICATION("PENDING_VERIFICATION"),
    ACTIVE("ACTIVE"),
    BANNED("BANNED");


    private final String value;
}