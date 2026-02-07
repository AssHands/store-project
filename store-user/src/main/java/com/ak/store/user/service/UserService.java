package com.ak.store.user.service;

import com.ak.store.user.model.dto.UserDTO;
import com.ak.store.user.model.command.WriteUserCommand;
import com.ak.store.user.model.entity.User;
import com.ak.store.user.model.entity.UserStatus;
import com.ak.store.user.model.entity.VerificationCode;
import com.ak.store.user.repository.UserRepo;
import com.ak.store.user.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    private User findOneWithVerificationCodeById(UUID id) {
        return userRepo.findOneWithVerificationCodeById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    private User findOneByVerificationCode(String code) {
        return userRepo.findOneByVerificationCode(code)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    private User findOneById(UUID id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public UserDTO findOne(UUID id) {
        return userMapper.toDTO(findOneById(id));
    }

    public UserDTO updateOne(WriteUserCommand command) {
        var user = findOneById(command.getUserId());

        user.setName(command.getName());

        return userMapper.toDTO(userRepo.save(user));
    }

    @Transactional
    public String makeVerificationCode(UUID id, String email) {
        var user = findOneWithVerificationCodeById(id);
        String code = UUID.randomUUID().toString();
        var expireTime = LocalDateTime.now().plusDays(1L);

        user.setStatus(UserStatus.PENDING_VERIFICATION);
        user.setVerificationCode(VerificationCode.builder()
                .code(code)
                //todo заменить на User.Builder()? чтобы цикличности не было
                .user(user)
                .expireTime(expireTime)
                .email(email)
                .build()
        );

        userRepo.save(user);
        return code;
    }

    @Transactional
    public UserDTO verifyOne(String code) {
        var user = findOneByVerificationCode(code);
        var nowTime = LocalDateTime.now();

        if (user.getVerificationCode().getExpireTime().isBefore(nowTime)) {
            throw new RuntimeException("this verify code is expired");
        }

        String email = user.getVerificationCode().getEmail();
        user.setEmail(email);
        user.setStatus(UserStatus.ACTIVE);
        user.setVerificationCode(null);

        return userMapper.toDTO(userRepo.save(user));
    }

    public Boolean isExistOne(UUID id) {
        return userRepo.existsOneById(id);
    }
}
