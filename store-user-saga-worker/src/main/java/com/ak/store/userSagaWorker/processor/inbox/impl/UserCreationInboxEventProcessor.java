package com.ak.store.userSagaWorker.processor.inbox.impl;

import com.ak.store.common.snapshot.user.UserVerificationSnapshot;
import com.ak.store.userSagaWorker.model.dto.UserCreationSagaRequestEvent;
import com.ak.store.userSagaWorker.model.entity.InboxEvent;
import com.ak.store.userSagaWorker.model.entity.InboxEventType;
import com.ak.store.userSagaWorker.model.entity.OutboxEventType;
import com.ak.store.userSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.userSagaWorker.service.OutboxEventService;
import com.ak.store.userSagaWorker.service.UserService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserCreationInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final UserService userService;
    private final OutboxEventService outboxEventService;

    @Override
    public void process(InboxEvent event) {
        var request = gson.fromJson(event.getPayload(), UserCreationSagaRequestEvent.class);

        userService.createOne(request.getUserId(), request.getEmail(), request.getName());

        userService.makeVerificationCode(request.getUserId(), request.getEmail(), request.getVerificationCode());

        var snapshot = UserVerificationSnapshot.builder()
                .userId(request.getUserId())
                .email(request.getEmail())
                .verificationCode(request.getVerificationCode())
                .build();

        outboxEventService.createOne(UUID.randomUUID(), snapshot, OutboxEventType.USER_VERIFICATION);
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.USER_CREATION;
    }
}
