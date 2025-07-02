package com.ak.store.sagaOrchestrator.service;

import com.ak.store.sagaOrchestrator.model.entity.SagaStep;
import com.ak.store.sagaOrchestrator.model.entity.SagaStepStatus;
import com.ak.store.sagaOrchestrator.repository.SagaStepRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SagaStepService {
    private final SagaStepRepo sagaStepRepo;

    private Integer batchSize = 100;

    public List<SagaStep> findAllForProcessing() {
        Pageable pageable = PageRequest.of(0, batchSize);
        return sagaStepRepo.findAllForProcessing(SagaStepStatus.IN_PROGRESS, pageable);
    }

    @Transactional
    public void continueOne(UUID sagaId, String stepName) {
        sagaStepRepo.saveOneIgnoreDuplicate(stepName, false,
                SagaStepStatus.IN_PROGRESS.getValue(), sagaId, LocalDateTime.now());
    }

    @Transactional
    public void compensateOne(UUID sagaId, String stepName) {
        sagaStepRepo.saveOneIgnoreDuplicate(stepName, true,
                SagaStepStatus.IN_PROGRESS.getValue(), sagaId, LocalDateTime.now());
    }

    @Transactional
    public void compensateAll(List<SagaStep> sagaSteps) {
        for (var sagaStep : sagaSteps) {
            sagaStep.setIsCompensation(true);
            sagaStep.setTime(LocalDateTime.now());
            sagaStep.setStatus(SagaStepStatus.IN_PROGRESS);
        }

        sagaStepRepo.saveAll(sagaSteps);
    }

    @Transactional
    public void markAllAsCompleted(List<SagaStep> sagaSteps) {
        sagaStepRepo.updateAll(sagaSteps, SagaStepStatus.COMPLETED);
    }
}