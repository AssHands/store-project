package com.ak.store.userRegistrationSagaWorker.processor.inbox.impl;

import com.ak.store.userRegistrationSagaWorker.model.dto.CancelUserRegistrationSagaRequestEvent;
import com.ak.store.userRegistrationSagaWorker.model.entity.InboxEvent;
import com.ak.store.userRegistrationSagaWorker.model.entity.InboxEventType;
import com.ak.store.userRegistrationSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.userRegistrationSagaWorker.service.UserService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelUserRegistrationInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final UserService userService;

    @Override
    public void process(InboxEvent event) {
        var cancelUserRegistrationRequest = gson.fromJson(event.getPayload(), CancelUserRegistrationSagaRequestEvent.class);
        userService.deleteOne(cancelUserRegistrationRequest.getUserId());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_USER_REGISTRATION;
    }
}
