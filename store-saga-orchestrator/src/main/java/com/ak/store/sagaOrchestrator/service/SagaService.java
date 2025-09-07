package com.ak.store.sagaOrchestrator.service;

import com.ak.store.sagaOrchestrator.model.Saga;
import com.ak.store.sagaOrchestrator.model.SagaStatus;
import com.ak.store.sagaOrchestrator.repository.SagaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SagaService {
    private final SagaRepo sagaRepo;

    private Integer batchSize = 100;

    public <T> void createOne(UUID sagaId, String sagaName, String payload) {
        sagaRepo.saveOneIgnoreDuplicate(sagaId, sagaName, payload, SagaStatus.IN_PROGRESS.getValue(), LocalDateTime.now());
    }

    public void markAllAs(List<Saga> sagas, SagaStatus status) {
        sagaRepo.updateAllStatus(sagas, status);
    }

    public void markOneAs(UUID sagaId, SagaStatus status) {
        sagaRepo.updateOneStatusById(sagaId, status);
    }
}