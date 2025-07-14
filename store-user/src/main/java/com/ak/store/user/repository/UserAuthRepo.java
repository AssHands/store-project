package com.ak.store.user.repository;

import java.util.UUID;

public interface UserAuthRepo {
    UUID registerOne(String email, String password);

    void verifyOne(UUID id, String email);
}
