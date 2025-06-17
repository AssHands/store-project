package com.ak.store.sagaOrchestrator.service;

import com.ak.store.common.saga.SagaEvent;
import com.ak.store.sagaOrchestrator.repository.SagaRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SagaService {
    private final SagaRepo sagaRepo;

    @Transactional
    public <T extends SagaEvent> void createOne(UUID sagaId, String stepName, T payload) {

    }

    @Transactional
    public void continueOne(UUID sagaId, String stepName) {

    }

    @Transactional
    public void compensateOne(UUID sagaId, String stepName) {

    }
}
