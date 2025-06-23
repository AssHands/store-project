package com.ak.store.sagaOrchestrator.scheduler;

import com.ak.store.sagaOrchestrator.model.entity.Saga;
import com.ak.store.sagaOrchestrator.model.entity.SagaStatus;
import com.ak.store.sagaOrchestrator.model.entity.SagaStep;
import com.ak.store.sagaOrchestrator.service.SagaProcessor;
import com.ak.store.sagaOrchestrator.service.SagaService;
import com.ak.store.sagaOrchestrator.service.SagaStepService;
import com.ak.store.sagaOrchestrator.util.SagaProperties;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SagaScheduler {
    private final SagaStepService sagaStepService;
    private final SagaService sagaService;
    private final SagaProcessor sagaProcessor;
    private final SagaProperties sagaProperties;

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void executeSaga() {
        var sagas = sagaService.findAllForProcessing();
        List<Saga> completed = new ArrayList<>();
        List<Saga> failed = new ArrayList<>();

        for (var saga : sagas) {
            if (!isSagaValid(saga)) {
                failed.add(saga);
                continue;
            }

            if (sagaProcessor.handleSaga(saga)) {
                completed.add(saga);
            }
        }

        sagaService.markAllAs(completed, SagaStatus.IN_PROGRESS);
        sagaService.markAllAs(failed, SagaStatus.FAILED);
    }

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void executeFailedSaga() {
        var sagas = sagaService.findAllFailedForProcessing();
        List<Saga> completed = new ArrayList<>();

        for (var saga : sagas) {
            if (sagaProcessor.handleFailedSaga(saga)) {
                completed.add(saga);
            }
        }

        sagaService.markAllAs(completed, SagaStatus.COMPLETED);
    }

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void executeSagaStep() {
        var sagaSteps = sagaStepService.findAllForProcessing();
        List<SagaStep> completed = new ArrayList<>();
        List<SagaStep> failed = new ArrayList<>();

        for (var sagaStep : sagaSteps) {
            if (!isSagaStepValid(sagaStep)) {
                failed.add(sagaStep);
                continue;
            }

            if (sagaProcessor.handleSagaStep(sagaStep)) {
                completed.add(sagaStep);
            }
        }

        sagaStepService.compensateAll(failed);
        sagaStepService.markAllAsCompleted(completed);
    }

    private boolean isSagaValid(Saga saga) {
        var sagaDef = sagaProperties.getDefinition(saga.getName());

        if (sagaDef == null) {
            return false;
        }

        var afterTime = LocalDateTime.now().minusMinutes(sagaDef.getTimeout());
        return !saga.getTime().isBefore(afterTime);
    }

    private boolean isSagaStepValid(SagaStep sagaStep) {
        var stepDef = sagaProperties.getCurrentStep(sagaStep.getSaga().getName(), sagaStep.getName());

        if (stepDef == null) {
            return false;
        }

        var afterTime = LocalDateTime.now().minusMinutes(stepDef.getTimeout());
        return !sagaStep.getTime().isBefore(afterTime);
    }
}