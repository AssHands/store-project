package com.ak.store.user.service;

import com.ak.store.user.model.dto.UserDTO;
import com.ak.store.user.model.dto.write.UserWriteDTO;
import com.ak.store.user.model.entity.User;
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
        return userMapper.toUserDTO(findOneById(id));
    }

    public UserDTO updateOne(UUID id, UserWriteDTO request) {
        var user = findOneById(id);
        updateOneFromDTO(user, request);
        return userMapper.toUserDTO(userRepo.save(user));
    }

    @Transactional
    public void deleteOne(UUID id) {
        userRepo.deleteById(id);
    }

    @Transactional
    public String makeVerificationCode(UUID id, String email) {
        var user = findOneWithVerificationCodeById(id);
        String code = UUID.randomUUID().toString();
        var expireTime = LocalDateTime.now().plusDays(1L);

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

        //todo менять статус, а не true
        //user.setIsEnabled(true);
        String email = user.getVerificationCode().getEmail();
        user.setEmail(email);
        user.setVerificationCode(null);

        return userMapper.toUserDTO(userRepo.save(user));
    }

    public Boolean isExistOne(UUID id) {
        return userRepo.existsOneById(id);
    }

    private void updateOneFromDTO(User user, UserWriteDTO request) {
        if (request.getName() != null) {
            user.setName(request.getName());
        }
    }
}
