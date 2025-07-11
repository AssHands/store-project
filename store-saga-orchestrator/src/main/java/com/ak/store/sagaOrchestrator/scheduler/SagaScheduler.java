package com.ak.store.sagaOrchestrator.scheduler;

import com.ak.store.sagaOrchestrator.model.entity.SagaStep;
import com.ak.store.sagaOrchestrator.model.entity.SagaStepStatus;
import com.ak.store.sagaOrchestrator.processor.SagaProcessor;
import com.ak.store.sagaOrchestrator.service.SagaStepService;
import com.ak.store.sagaOrchestrator.util.SagaProperties;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SagaScheduler {
    private final SagaStepService sagaStepService;
    private final SagaProcessor sagaProcessor;
    private final SagaProperties sagaProperties;

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void executeSagaStep() {
        var sagaSteps = sagaStepService.findAllForProcessing(SagaStepStatus.IN_PROGRESS);
        List<SagaStep> expired = new ArrayList<>();

        for (var step : sagaSteps) {
            if (isSagaStepExpired(step)) {
                expired.add(step);
                continue;
            }

            sagaProcessor.handleSagaStep(step);
        }

        sagaStepService.markAllAs(expired, SagaStepStatus.EXPIRED);
    }

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void executeExpiredSagaStep() {
        var sagaSteps = sagaStepService.findAllForProcessing(SagaStepStatus.EXPIRED);
        List<SagaStep> completed = new ArrayList<>();

        for (var step : sagaSteps) {
            String sagaName = step.getSaga().getName();
            String stepName = step.getName();
            UUID sagaId = step.getSaga().getId();

            var stepDef = sagaProperties.getPreviousStep(sagaName, stepName);

            try {
                if (stepDef != null) {
                    sagaStepService.createOne(sagaId, stepDef.getName(), true);
                } else {
                    sagaStepService.createOne(sagaId, sagaName, true);
                }

                completed.add(step);
            } catch (Exception ignored) {}
        }

        sagaStepService.markAllAs(completed, SagaStepStatus.FAILED);
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