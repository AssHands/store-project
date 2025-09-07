package com.ak.store.sagaOrchestrator.scheduler;

import com.ak.store.sagaOrchestrator.model.SagaStep;
import com.ak.store.sagaOrchestrator.model.SagaStepStatus;
import com.ak.store.sagaOrchestrator.processor.SagaProcessor;
import com.ak.store.sagaOrchestrator.service.SagaStepService;
import com.ak.store.sagaOrchestrator.util.SagaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SagaScheduler {
    private final SagaStepService sagaStepService;
    private final SagaProcessor sagaProcessor;
    private final SagaProperties sagaProperties;

    @Scheduled(fixedRate = 5000)
    public void executeSagaStep() {
        var sagaSteps = sagaStepService.findAllForProcessing(SagaStepStatus.IN_PROGRESS);

        for (var step : sagaSteps) {
            if (isSagaStepExpired(step)) {
                sagaStepService.markOneAs(step, SagaStepStatus.EXPIRED);
                continue;
            }

            sagaProcessor.handleSagaStep(step);
        }
    }

    @Scheduled(fixedRate = 5000)
    public void executeExpiredSagaStep() {
        var sagaSteps = sagaStepService.findAllForProcessing(SagaStepStatus.EXPIRED);

        for (var step : sagaSteps) {
            String sagaName = step.getSaga().getName();
            String stepName = step.getName();
            UUID sagaId = step.getSaga().getId();

            var stepDef = sagaProperties.getPreviousStep(sagaName, stepName);

            try {
                if (stepDef != null) {
                    sagaStepService.createOne(sagaId, stepDef.getName(), true);
                    sagaStepService.markOneAs(step, SagaStepStatus.COMPLETED);
                } else {
                    sagaStepService.createOne(sagaId, sagaName, true);
                    sagaStepService.markOneAs(step, SagaStepStatus.FAILED);
                }
            } catch (Exception ignored) {
            }
        }
    }

    //todo перенести валидацию в отдельный класс
    private boolean isSagaStepExpired(SagaStep sagaStep) {
        if (sagaStep.getIsCompensation() || sagaStep.getSaga().getName().equals(sagaStep.getName())) {
            return false;
        }

        var stepDef = sagaProperties.getCurrentStep(sagaStep.getSaga().getName(), sagaStep.getName());
        LocalDateTime afterTime = LocalDateTime.now().minusMinutes(stepDef.getTimeout());
        return sagaStep.getTime().isBefore(afterTime);
    }
}