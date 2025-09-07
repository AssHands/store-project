package com.ak.store.sagaOrchestrator.service;

import com.ak.store.sagaOrchestrator.model.SagaStep;
import com.ak.store.sagaOrchestrator.model.SagaStepStatus;
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
    private Integer retryTimeMinutes = 5;


    @Transactional
    public void markOneAs(UUID stepId, SagaStepStatus status) {
        sagaStepRepo.updateOneStatusById(stepId, status, LocalDateTime.now());
    }

    @Transactional
    public void markAllAs(List<SagaStep> steps, SagaStepStatus status) {
        sagaStepRepo.updateAllStatus(steps, status, LocalDateTime.now());
    }

    @Transactional
    public void markOneAs(SagaStep step, SagaStepStatus status) {
        sagaStepRepo.updateOneStatus(step, status, LocalDateTime.now());
    }

    @Transactional
    public void createOne(UUID sagaId, String stepName, Boolean isCompensation) {
        LocalDateTime now = LocalDateTime.now();
        sagaStepRepo.saveOneIgnoreDuplicate(sagaId, stepName, isCompensation,
                SagaStepStatus.IN_PROGRESS.getValue(), now, now);
    }

    @Transactional
    public List<SagaStep> findAllForProcessing(SagaStepStatus status) {
        LocalDateTime retryTime = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, batchSize);

        List<SagaStep> events = sagaStepRepo.findAllForProcessing(status, LocalDateTime.now(), pageable);

        for (var event : events) {
            event.setRetryTime(retryTime.plusMinutes(retryTimeMinutes));
        }

        return events;
    }
}