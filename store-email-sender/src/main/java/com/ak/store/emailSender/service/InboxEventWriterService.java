package com.ak.store.emailSender.service;

import com.ak.store.emailSender.inbox.InboxEventStatus;
import com.ak.store.emailSender.inbox.InboxEventType;
import com.ak.store.emailSender.repository.InboxEventRepo;
import com.ak.store.kafka.storekafkastarter.util.JsonMapperKafka;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class InboxEventWriterService {
    private final InboxEventRepo inboxEventRepo;
    private final JsonMapperKafka jsonMapperKafka;

    @Transactional
    public <T> void createOne(UUID eventId, T payload, InboxEventType type) {
        inboxEventRepo.saveOneIgnoreDuplicate(eventId, jsonMapperKafka.toJson(payload), type.getValue(),
                InboxEventStatus.IN_PROGRESS.getValue(), LocalDateTime.now());
    }
}