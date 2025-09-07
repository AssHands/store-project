package com.ak.store.paymentSagaWorker.service;

import com.ak.store.paymentSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.paymentSagaWorker.model.inbox.InboxEventType;
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
    public <T> void createOne(UUID eventId, String stepName, UUID sagaId, String sagaName, String payload, InboxEventType type) {
        inboxEventRepo.saveOneIgnoreDuplicate(eventId, stepName, sagaId, sagaName, payload, type.getValue(),
                InboxEventStatus.IN_PROGRESS.getValue(), LocalDateTime.now());
    }

    @Transactional
    public <T> void createOneFailure(UUID eventId, String stepName, UUID sagId, String sagaName, InboxEventType type) {
        inboxEventRepo.saveOneIgnoreDuplicate(eventId, stepName, sagId, sagaName, type.getValue(),
                InboxEventStatus.FAILURE.getValue(), LocalDateTime.now());
    }
}