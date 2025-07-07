package com.ak.store.userRegistration.facade;

import com.ak.store.common.snapshot.user.UserCreatedSnapshot;
import com.ak.store.userRegistration.model.dto.write.UserWriteDTO;
import com.ak.store.userRegistration.outbox.OutboxEventService;
import com.ak.store.userRegistration.outbox.OutboxEventType;
import com.ak.store.userRegistration.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserFacade {
    private final UserService userService;
    private final OutboxEventService outboxEventService;

    public void registerOne(UserWriteDTO request) {
        UUID id = userService.registerOne(request);

        var snapshot = UserCreatedSnapshot.builder()
                .userId(id)
                .email(request.getEmail())
                .name(request.getName())
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.USER_REGISTRATION);
    }
}