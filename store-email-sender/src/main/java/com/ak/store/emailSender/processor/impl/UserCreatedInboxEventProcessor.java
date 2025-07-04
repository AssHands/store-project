package com.ak.store.emailSender.processor.impl;

import com.ak.store.common.kafka.user.VerifyUserEvent;
import com.ak.store.emailSender.facade.EmailFacade;
import com.ak.store.emailSender.inbox.InboxEvent;
import com.ak.store.emailSender.inbox.InboxEventType;
import com.ak.store.emailSender.processor.InboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserCreatedInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final EmailFacade emailFacade;

    @Override
    public void process(InboxEvent event) {
        var verifyUserEvent = gson.fromJson(event.getPayload(), VerifyUserEvent.class);

        emailFacade.sendVerification(verifyUserEvent.getVerifyUser().getEmail(),
                verifyUserEvent.getVerifyUser().getVerificationCode());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.USER_CREATED;
    }
}
