package com.ak.store.userSagaWorker.processor.inbox.impl;

import com.ak.store.userSagaWorker.model.dto.ConfirmUserCreationSagaRequestEvent;
import com.ak.store.userSagaWorker.model.entity.InboxEvent;
import com.ak.store.userSagaWorker.model.entity.InboxEventType;
import com.ak.store.userSagaWorker.model.entity.UserStatus;
import com.ak.store.userSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.userSagaWorker.service.UserService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConfirmUserCreationInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final UserService userService;

    @Override
    public void process(InboxEvent event) {
        var confirmUserCreationRequest = gson.fromJson(event.getPayload(), ConfirmUserCreationSagaRequestEvent.class);
        userService.setStatus(confirmUserCreationRequest.getUserId(), UserStatus.PENDING_VERIFICATION);
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CONFIRM_USER_CREATION;
    }
}
