package com.ak.store.sagaOrchestrator.facade;

import com.ak.store.common.saga.SagaEvent;
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
    public <T extends SagaEvent> void createOne(UUID sagaId, T payload) {

    }

    @Transactional
    public void continueOne(UUID sagaId, String stepName) {

    }

    @Transactional
    public void compensateOne(UUID sagaId, String stepName) {

    }
}