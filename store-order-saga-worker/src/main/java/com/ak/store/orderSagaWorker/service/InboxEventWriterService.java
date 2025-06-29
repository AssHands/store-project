package com.ak.store.orderSagaWorker.service;

import com.ak.store.orderSagaWorker.model.entity.InboxEvent;
import com.ak.store.orderSagaWorker.model.entity.InboxEventStatus;
import com.ak.store.orderSagaWorker.model.entity.InboxEventType;
import com.ak.store.orderSagaWorker.repository.InboxEventRepo;
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