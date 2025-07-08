package com.ak.store.sagaOrchestrator.facade;

import com.ak.store.sagaOrchestrator.service.SagaService;
import com.ak.store.sagaOrchestrator.service.SagaStepService;
import com.ak.store.sagaOrchestrator.util.SagaProperties;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SagaFacade {
    private final SagaService sagaService;
    private final SagaStepService sagaStepService;
    private final SagaProperties sagaProperties;

    @Transactional
    public void createOne(UUID sagaId, String sagaName, String payload) {
        sagaService.createOne(sagaId, sagaName, payload);

        String stepName = sagaProperties.getFirstStep(sagaName).getName();
        sagaStepService.createOne(sagaId, stepName, false);
    }
}
