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

    public void markOneAs(UUID stepId, SagaStepStatus status) {
        sagaStepRepo.updateStatusById(stepId, status);
    }

    public void createOne(UUID sagaId, String stepName, Boolean isCompensation) {
        sagaStepRepo.saveOneIgnoreDuplicate(sagaId, stepName, isCompensation,
                SagaStepStatus.IN_PROGRESS.getValue(), LocalDateTime.now());
    }

    public List<SagaStep> findAllForProcessing() {
        Pageable pageable = PageRequest.of(0, batchSize);
        return sagaStepRepo.findAllForProcessing(SagaStepStatus.IN_PROGRESS, pageable);
    }
}