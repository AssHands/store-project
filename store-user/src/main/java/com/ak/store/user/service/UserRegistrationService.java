package com.ak.store.user.service;

import com.ak.store.user.model.dto.write.UserWriteDTO;
import com.ak.store.user.repository.UserRegistrationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserRegistrationService {
    private final UserRegistrationRepo userRegistrationRepo;

    public UUID registerOne(UserWriteDTO request) {
        return userRegistrationRepo.registerOne(request.getName(), request.getEmail(), request.getPassword());
    }
}
