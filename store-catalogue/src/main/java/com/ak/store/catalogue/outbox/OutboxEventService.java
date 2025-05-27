package com.ak.store.catalogue.outbox;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class OutboxEventService<T> {
    private final OutboxEventRepo outboxEventRepo;
    private final OutboxEventMapper<T> outboxEventMapper;

    @Transactional
    public void createOne(T payload, OutboxEventType type) {
        var event = outboxEventMapper.toOutboxEvent(payload);

        event.setType(type);
        event.setStatus(OutboxEventStatus.IN_PROGRESS);
        event.setRetryTime(LocalDateTime.now());

        outboxEventRepo.save(event);
    }
}