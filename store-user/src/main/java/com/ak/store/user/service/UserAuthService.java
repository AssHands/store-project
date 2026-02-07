package com.ak.store.user.service;

import com.ak.store.user.model.command.WriteUserCommand;
import com.ak.store.user.repository.UserAuthRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserAuthService {
    private final UserAuthRepo userAuthRepo;

    public UUID registerOne(WriteUserCommand command) {
        return userAuthRepo.registerOne(command.getEmail(), command.getPassword());
    }

    public void verifyOne(UUID id, String email) {
        userAuthRepo.verifyOne(id, email);
    }
}
