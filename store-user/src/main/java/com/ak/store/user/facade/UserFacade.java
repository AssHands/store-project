package com.ak.store.user.facade;

import com.ak.store.common.kafka.user.UserVerifyEvent;
import com.ak.store.common.snapshot.user.UserVerifySnapshot;
import com.ak.store.user.kafka.EventProducerKafka;
import com.ak.store.user.model.dto.UserDTO;
import com.ak.store.user.model.dto.write.UserWriteDTO;
import com.ak.store.user.service.UserKeycloakService;
import com.ak.store.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;
    private final UserKeycloakService userKeycloakService;
    private final EventProducerKafka eventProducerKafka;

    public UserDTO findOne(UUID id) {
        return userService.findOne(id);
    }

    @Transactional
    public UserDTO createOne(UserWriteDTO request) {
        UUID userId = null;

        try {
            userId = userKeycloakService.createOne(request);
            var user = userService.createOne(userId, request);
            String code = userService.makeVerificationCode(userId, user.getEmail());

            var event = UserVerifyEvent.builder()
                    .eventId(UUID.randomUUID())
                    .userVerify(UserVerifySnapshot.builder()
                            .email(user.getEmail())
                            .verificationCode(code)
                            .build())
                    .build();

            eventProducerKafka.send(event, user.getId().toString());

            return user;

        } catch (Exception e) {
            if (!Objects.isNull(userId))
                userKeycloakService.deleteOne(userId);

            throw new RuntimeException("error while creating consumer");
        }
    }

    @Transactional
    public UserDTO updateOne(UUID id, UserWriteDTO request) {
        userKeycloakService.updateOne(id, request);
        return userService.updateOne(id, request);
    }

    @Transactional
    //todo: не удлаять его отзывы.
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

        var event = UserVerifyEvent.builder()
                .eventId(UUID.randomUUID())
                .userVerify(UserVerifySnapshot.builder()
                        .email(email)
                        .verificationCode(code)
                        .build())
                .build();

        eventProducerKafka.send(event, id.toString());

        return userService.findOne(id);
    }
}