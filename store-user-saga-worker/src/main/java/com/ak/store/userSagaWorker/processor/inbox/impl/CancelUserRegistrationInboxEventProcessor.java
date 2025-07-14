package com.ak.store.userSagaWorker.processor.inbox.impl;


import com.ak.store.userSagaWorker.model.dto.CancelUserRegistrationSagaRequestEvent;
import com.ak.store.userSagaWorker.model.entity.InboxEvent;
import com.ak.store.userSagaWorker.model.entity.InboxEventType;
import com.ak.store.userSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.userSagaWorker.service.UserRegistrationService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelUserRegistrationInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final UserRegistrationService userRegistrationService;

    @Override
    public void process(InboxEvent event) {
        var cancelUserRegistrationRequest = gson.fromJson(event.getPayload(), CancelUserRegistrationSagaRequestEvent.class);
        userRegistrationService.deleteOne(cancelUserRegistrationRequest.getUserId());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_USER_REGISTRATION;
    }
}
