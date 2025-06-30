package com.ak.store.sagaOrchestrator.service;

import com.ak.store.sagaOrchestrator.model.entity.Saga;
import com.ak.store.sagaOrchestrator.model.entity.SagaStep;
import com.ak.store.sagaOrchestrator.model.entity.SagaStepStatus;
import com.ak.store.sagaOrchestrator.repository.SagaStepRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

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

    public void continueOne(UUID sagaId, String stepName) {
        var sagaStep = SagaStep.builder()
                .saga(Saga.builder()
                        .id(sagaId)
                        .build())
                .name(stepName)
                .isCompensation(false)
                .time(LocalDateTime.now())
                .status(SagaStepStatus.IN_PROGRESS)
                .build();

        try {
            sagaStepRepo.saveAndFlush(sagaStep);
        } catch (DataIntegrityViolationException e) {
            if (isDuplicateKeyException(e)) {
                return;
            }

            throw e;
        }
    }

    public void compensateOne(UUID sagaId, String stepName) {
        var sagaStep = SagaStep.builder()
                .saga(Saga.builder()
                        .id(sagaId)
                        .build())
                .name(stepName)
                .isCompensation(true)
                .time(LocalDateTime.now())
                .status(SagaStepStatus.IN_PROGRESS)
                .build();

        try {
            sagaStepRepo.saveAndFlush(sagaStep);
        } catch (DataIntegrityViolationException e) {
            if (isDuplicateKeyException(e)) {
                return;
            }

            throw e;
        }
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

    private boolean isDuplicateKeyException(Throwable e) {
        Throwable cause = e;
        while (cause != null) {
            if (cause instanceof org.hibernate.exception.ConstraintViolationException
                    && cause.getMessage() != null
                    && cause.getMessage().toLowerCase().contains("unique")) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }
}