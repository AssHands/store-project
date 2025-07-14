package com.ak.store.emailSender.processor.impl;

import com.ak.store.common.kafka.user.UserVerificationEvent;
import com.ak.store.common.snapshot.user.UserVerificationSnapshot;
import com.ak.store.emailSender.inbox.InboxEvent;
import com.ak.store.emailSender.inbox.InboxEventType;
import com.ak.store.emailSender.processor.InboxEventProcessor;
import com.ak.store.emailSender.service.EmailService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserVerificationInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final EmailService emailService;

    @Override
    public void process(InboxEvent event) {
        var message = new UserVerificationEvent(event.getId(),
                gson.fromJson(event.getPayload(), UserVerificationSnapshot.class));

        emailService.sendVerification(message.getRequest().getEmail(), message.getRequest().getVerificationCode());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.USER_VERIFICATION;
    }
}