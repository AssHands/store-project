package com.ak.store.emailSender.processor.impl;

import com.ak.store.common.event.user.UserVerifyEvent;
import com.ak.store.emailSender.facade.EmailFacade;
import com.ak.store.emailSender.inbox.InboxEvent;
import com.ak.store.emailSender.inbox.InboxEventType;
import com.ak.store.emailSender.processor.InboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserVerifyInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final EmailFacade emailFacade;

    @Override
    public void process(InboxEvent event) {
        var userVerifyEvent = gson.fromJson(event.getPayload(), UserVerifyEvent.class);

        emailFacade.sendVerification(userVerifyEvent.getEmail(), userVerifyEvent.getVerificationCode());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.USER_VERIFY;
    }
}
