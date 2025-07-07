package com.ak.store.userSagaWorker.processor.inbox.impl;

import com.ak.store.userSagaWorker.model.dto.CancelUserCreationSagaRequestEvent;
import com.ak.store.userSagaWorker.model.entity.InboxEvent;
import com.ak.store.userSagaWorker.model.entity.InboxEventType;
import com.ak.store.userSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.userSagaWorker.service.UserService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelUserCreationInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final UserService userService;

    @Override
    public void process(InboxEvent event) {
        var cancelUserCreationRequest = gson.fromJson(event.getPayload(), CancelUserCreationSagaRequestEvent.class);
        userService.deleteOne(cancelUserCreationRequest.getUserId());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_USER_CREATION;
    }
}
