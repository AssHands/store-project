package com.ak.store.userRegistration.repository;

import java.util.UUID;

public interface UserRepo {
    UUID registerOne(String name, String email, String password);
}
