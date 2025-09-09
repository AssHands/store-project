package com.ak.store.userSagaWorker.service;

import com.ak.store.userSagaWorker.model.user.User;
import com.ak.store.userSagaWorker.model.user.UserStatus;
import com.ak.store.userSagaWorker.model.user.VerificationCode;
import com.ak.store.userSagaWorker.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    private User findOneById(UUID id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    private User findOneWithVerificationCodeById(UUID id) {
        return userRepo.findOneWithVerificationCodeById(id)
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

    public void setStatus(UUID id, UserStatus status) {
        var user = findOneById(id);
        user.setStatus(status);
        userRepo.save(user);
    }

    public void makeVerificationCode(UUID id, String email, String verificationCode) {
        var user = findOneWithVerificationCodeById(id);
        var expireTime = LocalDateTime.now().plusDays(1L);

        user.setVerificationCode(VerificationCode.builder()
                .code(verificationCode)
                //todo заменить на User.Builder()? чтобы цикличности не было
                .user(user)
                .expireTime(expireTime)
                .email(email)
                .build()
        );

        userRepo.save(user);
    }
}
