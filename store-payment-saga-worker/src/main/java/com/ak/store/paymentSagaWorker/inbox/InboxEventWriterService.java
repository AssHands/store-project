package com.ak.store.paymentSagaWorker.inbox;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class InboxEventWriterService {
    private final InboxEventRepo inboxEventRepo;
    private final InboxEventMapper inboxEventMapper;

    @Transactional
    public <T> void createOne(UUID eventId, T payload, InboxEventType type) {
        var event = inboxEventMapper.toInboxEvent(payload);

        event.setType(type);
        event.setStatus(InboxEventStatus.IN_PROGRESS);
        event.setRetryTime(LocalDateTime.now());
        event.setId(eventId);

        inboxEventRepo.save(event);
    }
}