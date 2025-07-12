package com.ak.store.catalogueOutbox.service;

import com.ak.store.catalogueOutbox.model.OutboxEvent;
import com.ak.store.catalogueOutbox.model.OutboxEventStatus;
import com.ak.store.catalogueOutbox.model.OutboxEventType;
import com.ak.store.catalogueOutbox.repository.OutboxEventRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OutboxEventService {
    private final OutboxEventRepo outboxEventRepo;
    private Integer batchSize = 100;
    private Integer retryTimeMinutes = 5;

    @Transactional
    public List<OutboxEvent> findAllForProcessing(OutboxEventType type) {
        LocalDateTime retryTime = LocalDateTime.now();

        Pageable pageable = PageRequest.of(0, batchSize);
        List<OutboxEvent> events = outboxEventRepo.findAllForProcessing(
                type, OutboxEventStatus.IN_PROGRESS, LocalDateTime.now(), pageable);

        for (var event : events) {
            event.setRetryTime(retryTime.plusMinutes(retryTimeMinutes));
        }

        return events;
    }

    @Transactional
    public void markAllAsCompleted(List<OutboxEvent> events) {
        outboxEventRepo.updateAll(events, OutboxEventStatus.COMPLETED);
    }
}