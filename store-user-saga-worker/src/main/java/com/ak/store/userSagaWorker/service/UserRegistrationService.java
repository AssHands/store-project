package com.ak.store.userSagaWorker.service;

import com.ak.store.userSagaWorker.repository.UserRegistrationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private final UserRegistrationRepo userRegistrationRepo;

    public void deleteOne(UUID id) {
        userRegistrationRepo.deleteOne(id);
    }
}
