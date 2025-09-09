package com.ak.store.userSagaWorker.processor.inbox.impl;

import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.snapshot.user.UserCreationSnapshot;
import com.ak.store.kafka.storekafkastarter.model.snapshot.user.UserVerificationSnapshot;
import com.ak.store.userSagaWorker.model.dto.UserCreationSagaRequestEvent;
import com.ak.store.userSagaWorker.model.inbox.InboxEvent;
import com.ak.store.userSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.userSagaWorker.model.inbox.InboxEventType;
import com.ak.store.userSagaWorker.model.outbox.OutboxEventType;
import com.ak.store.userSagaWorker.model.user.UserStatus;
import com.ak.store.userSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.userSagaWorker.service.InboxEventReaderService;
import com.ak.store.userSagaWorker.service.OutboxEventService;
import com.ak.store.userSagaWorker.service.UserService;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserCreationInboxEventProcessor implements InboxEventProcessor {
    private final JsonMapperKafka jsonMapperKafka;
    private final InboxEventReaderService inboxEventReaderService;
    private final UserService userService;
    private final OutboxEventService outboxEventService;

    @Transactional
    @Override
    public void process(InboxEvent event) {
        var user = jsonMapperKafka.fromJson(event.getPayload(), UserCreationSnapshot.class);

        var verificationCode = UserVerificationSnapshot.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .verificationCode(user.getVerificationCode())
                .build();

        try {
            userService.createOne(user.getUserId(), user.getEmail(), user.getName());
            userService.makeVerificationCode(user.getUserId(), user.getEmail(), user.getVerificationCode());
            outboxEventService.createOne(UUID.randomUUID(), verificationCode, OutboxEventType.USER_VERIFICATION);
            inboxEventReaderService.markOneAs(event, InboxEventStatus.SUCCESS);
        } catch (Exception e) {
            inboxEventReaderService.markOneAsFailure(event);
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.USER_CREATION;
    }
}
