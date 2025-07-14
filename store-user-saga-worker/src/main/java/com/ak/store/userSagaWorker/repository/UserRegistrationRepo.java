package com.ak.store.userSagaWorker.repository;

import java.util.UUID;

public interface UserRegistrationRepo {
    void deleteOne(UUID id);
}
