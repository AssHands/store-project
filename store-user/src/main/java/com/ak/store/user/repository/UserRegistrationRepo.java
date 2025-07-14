package com.ak.store.user.repository;

import java.util.UUID;

public interface UserRegistrationRepo {
    UUID registerOne(String name, String email, String password);
}
