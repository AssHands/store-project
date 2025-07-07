package com.ak.store.user.facade;

import com.ak.store.common.snapshot.user.VerifyUserSnapshot;
import com.ak.store.user.model.dto.UserDTO;
import com.ak.store.user.model.dto.write.UserWriteDTO;
import com.ak.store.user.outbox.OutboxEventService;
import com.ak.store.user.outbox.OutboxEventType;
import com.ak.store.user.service.UserKeycloakService;
import com.ak.store.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;
    private final UserKeycloakService userKeycloakService;
    private final OutboxEventService outboxEventService;

    public UserDTO findOne(UUID id) {
        return userService.findOne(id);
    }

    @Transactional
    public UserDTO updateOne(UUID id, UserWriteDTO request) {
        userKeycloakService.updateOne(id, request);
        return userService.updateOne(id, request);
    }

    @Transactional
    public void deleteOne(UUID id) {
        userKeycloakService.deleteOne(id);
        userService.deleteOne(id);
    }

    public Boolean isExistOne(UUID id) {
        return userService.isExistOne(id);
    }

    @Transactional
    public UserDTO verifyOne(String code) {
        var user = userService.verifyOne(code);
        userKeycloakService.verifyOne(user.getId(), user.getEmail());

        return user;
    }

    @Transactional
    public UserDTO updateOneEmail(UUID id, String email) {
        String code = userService.makeVerificationCode(id, email);

        var event = VerifyUserSnapshot.builder()
                .id(id)
                .email(email)
                .verificationCode(code)
                .build();

        outboxEventService.createOne(event, OutboxEventType.VERIFY_USER);

        return userService.findOne(id);
    }
}