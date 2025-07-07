package com.ak.store.userRegistrationSagaWorker.service;

import com.ak.store.userRegistrationSagaWorker.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public void deleteOne(UUID id) {
        userRepo.deleteOne(id);
    }
}
