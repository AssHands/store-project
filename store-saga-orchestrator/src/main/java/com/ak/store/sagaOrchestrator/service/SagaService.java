package com.ak.store.sagaOrchestrator.service;

import com.ak.store.sagaOrchestrator.mapper.SagaMapper;
import com.ak.store.sagaOrchestrator.model.entity.Saga;
import com.ak.store.sagaOrchestrator.model.entity.SagaStatus;
import com.ak.store.sagaOrchestrator.model.entity.SagaStep;
import com.ak.store.sagaOrchestrator.model.entity.SagaStepStatus;
import com.ak.store.sagaOrchestrator.repository.SagaRepo;
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
public class SagaService {
    private final SagaRepo sagaRepo;
    private final SagaStepRepo sagaStepRepo;
    private final SagaMapper sagaMapper;

    private Integer batchSize = 100;

    @Transactional
    public <T> void createOne(UUID sagaId, String sagaName, T payload) {
        var saga = Saga.builder()
                .id(sagaId)
                .name(sagaName)
                .payload(sagaMapper.toPayload(payload))
                .time(LocalDateTime.now())
                .status(SagaStatus.RECEIVED)
                .build();

        sagaRepo.save(saga);
    }

    @Transactional
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

        sagaStepRepo.save(sagaStep);
    }

    @Transactional
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

        sagaStepRepo.save(sagaStep);
    }

    public List<Saga> findAllForProcessing() {
        Pageable pageable = PageRequest.of(0, batchSize);

        return sagaRepo.findAllForProcessing(
                SagaStatus.RECEIVED, pageable);
    }

    @Transactional
    public void markAllAsInProgress(List<Saga> sagas) {
        sagaRepo.updateAll(sagas, SagaStatus.IN_PROGRESS);
    }
}