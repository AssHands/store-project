package com.ak.store.catalogueSagaWorker.service;

import com.ak.store.catalogueSagaWorker.model.outbox.OutboxEventStatus;
import com.ak.store.catalogueSagaWorker.model.outbox.OutboxEventType;
import com.ak.store.catalogueSagaWorker.repository.OutboxEventRepo;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OutboxEventService {
    private final OutboxEventRepo outboxEventRepo;
    private final Gson gson;

    @Transactional
    public <T> void createOne(UUID id, T payload, OutboxEventType type) {
        outboxEventRepo.saveOneIgnoreDuplicate(id, gson.toJson(payload), type.getValue(),
                OutboxEventStatus.IN_PROGRESS.getStatus(), LocalDateTime.now());
    }
}