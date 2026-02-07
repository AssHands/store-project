package com.ak.store.emailSender.processor.impl;

import com.ak.store.emailSender.inbox.InboxEvent;
import com.ak.store.emailSender.inbox.InboxEventType;
import com.ak.store.emailSender.processor.InboxEventProcessor;
import com.ak.store.emailSender.service.EmailService;
import com.ak.store.kafka.storekafkastarter.model.event.user.UserVerificationEvent;
import com.ak.store.kafka.storekafkastarter.model.snapshot.user.UserVerificationSnapshot;
import com.ak.store.kafka.storekafkastarter.util.JsonMapperKafka;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserVerificationInboxEventProcessor implements InboxEventProcessor {
    private final EmailService emailService;
    private final JsonMapperKafka jsonMapperKafka;

    @Override
    public void process(InboxEvent event) {
        var message = new UserVerificationEvent(event.getId(),
                jsonMapperKafka.fromJson(event.getPayload(), UserVerificationSnapshot.class));

        emailService.sendVerification(message.getPayload().getEmail(), message.getPayload().getVerificationCode());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.USER_VERIFICATION;
    }
}