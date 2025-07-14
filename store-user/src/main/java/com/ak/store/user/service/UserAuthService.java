package com.ak.store.user.service;

import com.ak.store.user.model.dto.write.UserWriteDTO;
import com.ak.store.user.repository.UserAuthRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserAuthService {
    private final UserAuthRepo userAuthRepo;

    public UUID registerOne(UserWriteDTO request) {
        return userAuthRepo.registerOne(request.getEmail(), request.getPassword());
    }

    public void verifyOne(UUID id, String email) {
        userAuthRepo.verifyOne(id, email);
    }
}
