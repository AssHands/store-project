package com.ak.store.userRegistration.service;

import com.ak.store.userRegistration.model.dto.write.UserWriteDTO;
import com.ak.store.userRegistration.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepo userRepo;

    public UUID registerOne(UserWriteDTO request) {
        return userRepo.registerOne(request.getName(), request.getEmail(), request.getPassword());
    }
}
