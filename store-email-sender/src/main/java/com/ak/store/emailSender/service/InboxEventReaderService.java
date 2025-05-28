package com.ak.store.emailSender.service;

import com.ak.store.emailSender.inbox.InboxEvent;
import com.ak.store.emailSender.inbox.InboxEventStatus;
import com.ak.store.emailSender.inbox.InboxEventType;
import com.ak.store.emailSender.repository.InboxEventRepo;
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
                type, InboxEventStatus.IN_PROGRESS, LocalDateTime.now(), pageable);

        for (var event : events) {
            event.setRetryTime(retryTime.plusMinutes(retryTimeMinutes));
        }

        return events;
    }

    @Transactional
    public void markAllAsCompleted(List<InboxEvent> events) {
        inboxEventRepo.updateAll(events, InboxEventStatus.COMPLETED);
    }
}