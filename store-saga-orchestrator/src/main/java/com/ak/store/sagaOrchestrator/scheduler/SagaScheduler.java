package com.ak.store.sagaOrchestrator.scheduler;

import com.ak.store.sagaOrchestrator.model.entity.Saga;
import com.ak.store.sagaOrchestrator.model.entity.SagaStatus;
import com.ak.store.sagaOrchestrator.model.entity.SagaStep;
import com.ak.store.sagaOrchestrator.processor.SagaProcessor;
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
    public void executeSagaStep() {
        //todo добавить retry_time, иначе он будет постоянно доставать сообщения, которые уже были отправлены и отправлять их снова и снова без ожидания.
        var sagaSteps = sagaStepService.findAllForProcessing();
        List<SagaStep> failed = new ArrayList<>();

        for (var sagaStep : sagaSteps) {
            if (!isSagaStepValid(sagaStep)) {
                failed.add(sagaStep);
                continue;
            }

            sagaProcessor.handleSagaStep(sagaStep);
        }

        //todo помечать просроченные шаги как failed
    }

    //todo перенести валидацию в отдельный класс
    private boolean isSagaStepValid(SagaStep sagaStep) {
        var stepDef = sagaProperties.getCurrentStep(sagaStep.getSaga().getName(), sagaStep.getName());
        LocalDateTime afterTime;

        if (stepDef == null) {
            var sagaDef = sagaProperties.getDefinition(sagaStep.getName());
            afterTime = LocalDateTime.now().minusMinutes(sagaDef.getTimeout());
        } else {
            afterTime = LocalDateTime.now().minusMinutes(stepDef.getTimeout());
        }

        return !sagaStep.getTime().isBefore(afterTime);
    }
}