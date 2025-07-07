package com.ak.store.userSagaWorker.processor.inbox.impl;

import com.ak.store.userSagaWorker.model.dto.UserCreationSagaRequestEvent;
import com.ak.store.userSagaWorker.model.entity.InboxEvent;
import com.ak.store.userSagaWorker.model.entity.InboxEventType;
import com.ak.store.userSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.userSagaWorker.service.UserService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserCreationInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final UserService userService;

    @Override
    public void process(InboxEvent event) {
        var userCreationRequest = gson.fromJson(event.getPayload(), UserCreationSagaRequestEvent.class);
        userService.createOne(userCreationRequest.getUserId(),
                userCreationRequest.getEmail(), userCreationRequest.getName());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.USER_CREATION;
    }
}
