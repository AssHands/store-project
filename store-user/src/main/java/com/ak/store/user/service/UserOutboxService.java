package com.ak.store.user.service;

import com.ak.store.kafka.storekafkastarter.model.snapshot.user.UserVerificationSnapshot;
import com.ak.store.user.model.entity.User;
import com.ak.store.user.outbox.OutboxEventService;
import com.ak.store.user.outbox.OutboxEventType;
import com.ak.store.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserOutboxService {
    private final OutboxEventService outboxEventService;
    private final UserRepo userRepo;

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveUpdatedEmailEvent(UUID id) {
        var user = findOne(id);

        var snapshot = UserVerificationSnapshot.builder()
                .userId(id)
                .email(user.getEmail())
                .verificationCode(user.getVerificationCode().getCode())
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.USER_VERIFICATION);
    }

    public void saveRegisterEvent(UUID id) {
        var user = findOne(id);

        var snapshot = UserVerificationSnapshot.builder()
                .verificationCode(UUID.randomUUID().toString())
                .userId(id)
                .email(user.getEmail())
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.USER_REGISTRATION);
    }

    private User findOne(UUID id) {
        return userRepo.findOneWithVerificationCodeById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }
}
