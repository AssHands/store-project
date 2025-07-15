package com.ak.store.SynchronizationSagaWorker.service;

import com.ak.store.SynchronizationSagaWorker.model.entity.InboxEvent;
import com.ak.store.SynchronizationSagaWorker.model.entity.InboxEventStatus;
import com.ak.store.SynchronizationSagaWorker.model.entity.InboxEventType;
import com.ak.store.SynchronizationSagaWorker.repository.InboxEventRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InboxEventReaderService {
    private final InboxEventRepo inboxEventRepo;
    private Integer batchSize = 100;
    private Integer retryTimeMinutes = 5;

    @Transactional
    public List<InboxEvent> findAllForProcessing(InboxEventType type) {
        LocalDateTime retryTime = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, batchSize);

        List<InboxEvent> events = inboxEventRepo.findAllForProcessing(
                type, List.of(InboxEventStatus.IN_PROGRESS), LocalDateTime.now(), pageable
        );

        for (var event : events) {
            event.setRetryTime(retryTime.plusMinutes(retryTimeMinutes));
        }

        return events;
    }

    @Transactional
    public List<InboxEvent> findAllCompletedForProcessing(InboxEventType type) {
        LocalDateTime retryTime = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, batchSize);

        List<InboxEvent> events = inboxEventRepo.findAllForProcessing(
                type, List.of(InboxEventStatus.SUCCESS, InboxEventStatus.FAILURE), LocalDateTime.now(), pageable
        );

        for (var event : events) {
            event.setRetryTime(retryTime.plusMinutes(retryTimeMinutes));
        }

        return events;
    }

    @Transactional
    public void markAllAs(List<InboxEvent> events, InboxEventStatus status) {
        inboxEventRepo.updateAll(events, status, LocalDateTime.now());
    }
}