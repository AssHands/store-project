package com.ak.store.paymentSagaWorker.service;

import com.ak.store.paymentSagaWorker.model.entity.InboxEvent;
import com.ak.store.paymentSagaWorker.model.entity.InboxEventStatus;
import com.ak.store.paymentSagaWorker.model.entity.InboxEventType;
import com.ak.store.paymentSagaWorker.repository.InboxEventRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class InboxEventWriterService {
    private final InboxEventRepo inboxEventRepo;

    @Transactional
    public <T> void createOne(UUID eventId, String stepName, String payload, InboxEventType type) {
        var event = InboxEvent.builder()
                .id(eventId)
                .stepName(stepName)
                .payload(payload)
                .type(type)
                .status(InboxEventStatus.IN_PROGRESS)
                .retryTime(LocalDateTime.now())
                .build();

        inboxEventRepo.save(event);
    }
}