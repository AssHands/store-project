package com.ak.store.sagaOrchestrator.facade;

import com.ak.store.sagaOrchestrator.model.SagaStatus;
import com.ak.store.sagaOrchestrator.model.SagaStepStatus;
import com.ak.store.sagaOrchestrator.service.SagaService;
import com.ak.store.sagaOrchestrator.service.SagaStepService;
import com.ak.store.sagaOrchestrator.util.SagaProperties;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SagaStepFacade {
    private final SagaService sagaService;
    private final SagaStepService sagaStepService;
    private final SagaProperties sagaProperties;

    @Transactional
    public void continueOne(UUID sagaId, String sagaName, UUID stepId, String stepName) {
        sagaStepService.markOneAs(stepId, SagaStepStatus.COMPLETED);
        var stepDef = sagaProperties.getNextStep(sagaName, stepName);

        if (stepDef != null) {
            sagaStepService.createOne(sagaId, stepDef.getName(), false);
        } else {
            sagaService.markOneAs(sagaId, SagaStatus.COMPLETED);
        }
    }

    @Transactional
    public void startCompensation(UUID sagaId, String sagaName, UUID stepId, String stepName) {
        sagaStepService.markOneAs(stepId, SagaStepStatus.FAILED);

        if(sagaName.equals(stepName)) {
            sagaStepService.markOneAs(stepId, SagaStepStatus.COMPLETED);
            sagaService.markOneAs(sagaId, SagaStatus.COMPLETED);
            return;
        }

        var stepDef = sagaProperties.getPreviousStep(sagaName, stepName);

        if(stepDef != null) {
            sagaStepService.createOne(sagaId, stepDef.getName(), true);
        } else {
            sagaStepService.createOne(sagaId, sagaName, true);
        }
    }

    @Transactional
    public void compensateOne(UUID sagaId, String sagaName, UUID stepId, String stepName) {
        sagaStepService.markOneAs(stepId, SagaStepStatus.COMPLETED);

        if(sagaName.equals(stepName)) {
            sagaStepService.markOneAs(stepId, SagaStepStatus.COMPLETED);
            sagaService.markOneAs(sagaId, SagaStatus.COMPLETED);
            return;
        }

        var stepDef = sagaProperties.getPreviousStep(sagaName, stepName);

        if(stepDef != null) {
            sagaStepService.createOne(sagaId, stepDef.getName(), true);
        } else {
            sagaStepService.createOne(sagaId, sagaName, true);
        }
    }
}
