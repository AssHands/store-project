package com.ak.store.userSagaWorker.service;

import com.ak.store.userSagaWorker.model.entity.User;
import com.ak.store.userSagaWorker.model.entity.UserStatus;
import com.ak.store.userSagaWorker.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    private User findOneById(UUID id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public void createOne(UUID id, String email, String name) {
        if (userRepo.existsById(id)) {
            return;
        }

        var user = User.builder()
                .id(id)
                .email(email)
                .name(name)
                .status(UserStatus.PENDING_REGISTRATION)
                .build();

        userRepo.save(user);
    }

    public void deleteOne(UUID id) {
        userRepo.deleteById(id);
    }

    public void confirmOne(UUID id) {
        var user = findOneById(id);
        user.setStatus(UserStatus.ACTIVE);
        userRepo.save(user);
    }
}
