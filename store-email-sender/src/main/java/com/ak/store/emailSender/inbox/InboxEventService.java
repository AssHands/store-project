package com.ak.store.emailSender.inbox;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class InboxEventService<T> {
    private final InboxEventRepo inboxEventRepo;
    private final InboxEventMapper<T> inboxEventMapper;

    @Transactional
    public void createOne(UUID eventId, T payload, InboxEventType type) {
        var event = inboxEventMapper.toInboxEvent(payload);

        event.setType(type);
        event.setStatus(InboxEventStatus.IN_PROGRESS);
        event.setRetryTime(LocalDateTime.now());
        event.setId(eventId);

        inboxEventRepo.save(event);
    }

    @Transactional
    public void createAll(List<T> payloads, InboxEventType type) {
        List<InboxEvent> events = new ArrayList<>();

        for(var payload : payloads) {
            var event = inboxEventMapper.toInboxEvent(payload);

            event.setType(type);
            event.setStatus(InboxEventStatus.IN_PROGRESS);
            event.setRetryTime(LocalDateTime.now());

            events.add(event);
        }

        inboxEventRepo.saveAll(events);
    }
}