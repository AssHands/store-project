package com.ak.store.userSagaWorker.processor.inbox.impl;

import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.snapshot.user.UserCreationSnapshot;
import com.ak.store.userSagaWorker.model.dto.CancelUserRegistrationSagaRequestEvent;
import com.ak.store.userSagaWorker.model.inbox.InboxEvent;
import com.ak.store.userSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.userSagaWorker.model.inbox.InboxEventType;
import com.ak.store.userSagaWorker.model.user.UserStatus;
import com.ak.store.userSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.userSagaWorker.service.InboxEventReaderService;
import com.ak.store.userSagaWorker.service.UserRegistrationService;
import com.ak.store.userSagaWorker.service.UserService;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConfirmUserCreationInboxEventProcessor implements InboxEventProcessor {
    private final JsonMapperKafka jsonMapperKafka;
    private final InboxEventReaderService inboxEventReaderService;
    private final UserService userService;

    @Transactional
    @Override
    public void process(InboxEvent event) {
        var snapshot = jsonMapperKafka.fromJson(event.getPayload(), UserCreationSnapshot.class);

        try {
            userService.setStatus(snapshot.getUserId(), UserStatus.PENDING_VERIFICATION);
            inboxEventReaderService.markOneAs(event, InboxEventStatus.SUCCESS);
        } catch (Exception e) {
            inboxEventReaderService.markOneAsFailure(event);
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CONFIRM_USER_CREATION;
    }
}
