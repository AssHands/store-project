package com.ak.store.userRegistrationSagaWorker.repository;

import java.util.UUID;

public interface UserRepo {
    void deleteOne(UUID id);
}
