package com.ak.store.user.facade;

import com.ak.store.common.snapshot.user.UserCreationSnapshot;
import com.ak.store.common.snapshot.user.UserVerificationSnapshot;
import com.ak.store.user.model.dto.UserDTO;
import com.ak.store.user.model.dto.write.UserWriteDTO;
import com.ak.store.user.outbox.OutboxEventService;
import com.ak.store.user.outbox.OutboxEventType;
import com.ak.store.user.service.UserAuthService;
import com.ak.store.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;
    private final OutboxEventService outboxEventService;
    private final UserAuthService userAuthService;

    public UserDTO findOne(UUID id) {
        return userService.findOne(id);
    }

    @Transactional
    public UserDTO updateOne(UUID id, UserWriteDTO request) {
        return userService.updateOne(id, request);
    }

    public Boolean isExistOne(UUID id) {
        return userService.isExistOne(id);
    }

    @Transactional
    public UserDTO verifyOne(String code) {
        var user = userService.verifyOne(code);
        userAuthService.verifyOne(user.getId(), user.getEmail());

        return user;
    }

    @Transactional
    public void updateOneEmail(UUID id, String email) {
        String code = userService.makeVerificationCode(id, email);

        var event = UserVerificationSnapshot.builder()
                .userId(id)
                .email(email)
                .verificationCode(code)
                .build();

        outboxEventService.createOne(event, OutboxEventType.USER_VERIFICATION);
    }

    public void registerOne(UserWriteDTO request) {
        UUID id = userAuthService.registerOne(request);

        var snapshot = UserCreationSnapshot.builder()
                .verificationCode(UUID.randomUUID().toString())
                .userId(id)
                .email(request.getEmail())
                .name(request.getName())
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.USER_REGISTRATION);
    }
}