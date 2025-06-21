package com.ak.store.sagaOrchestrator.facade;

import com.ak.store.sagaOrchestrator.service.SagaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SagaFacade {
    private final SagaService sagaService;

    @Transactional
    public <T> void createOne(UUID sagaId, String sagaName, T payload) {
        sagaService.createOne(sagaId, sagaName, payload);
    }

    @Transactional
    public void continueOne(UUID sagaId, String stepName) {
        sagaService.continueOne(sagaId, stepName);
    }

    @Transactional
    public void compensateOne(UUID sagaId, String stepName) {
        sagaService.compensateOne(sagaId, stepName);
    }
}