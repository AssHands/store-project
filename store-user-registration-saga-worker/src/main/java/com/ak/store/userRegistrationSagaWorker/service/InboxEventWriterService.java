package com.ak.store.userRegistrationSagaWorker.service;

import com.ak.store.userRegistrationSagaWorker.model.entity.InboxEventStatus;
import com.ak.store.userRegistrationSagaWorker.model.entity.InboxEventType;
import com.ak.store.userRegistrationSagaWorker.repository.InboxEventRepo;
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
    public <T> void createOne(UUID eventId, UUID sagaId, String stepName, String payload, InboxEventType type) {
        inboxEventRepo.saveOneIgnoreDuplicate(eventId, sagaId, stepName, payload, type.getValue(),
                InboxEventStatus.IN_PROGRESS.getValue(), LocalDateTime.now());
    }

    @Transactional
    public <T> void createOneFailure(UUID eventId, UUID sagaId, String stepName, InboxEventType type) {
        inboxEventRepo.saveOneIgnoreDuplicate(eventId, sagaId, stepName, type.getValue(),
                InboxEventStatus.FAILURE.getValue(), LocalDateTime.now());
    }
}