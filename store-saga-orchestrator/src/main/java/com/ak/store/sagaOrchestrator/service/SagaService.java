package com.ak.store.sagaOrchestrator.service;

import com.ak.store.sagaOrchestrator.model.entity.Saga;
import com.ak.store.sagaOrchestrator.model.entity.SagaStatus;
import com.ak.store.sagaOrchestrator.repository.SagaRepo;
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

    private Integer batchSize = 100;

    @Transactional
    public <T> void createOne(UUID sagaId, String sagaName, String payload) {
        sagaRepo.insertIgnoreDuplicate(sagaId, sagaName, payload, SagaStatus.RECEIVED.getValue(), LocalDateTime.now());
    }

    public List<Saga> findAllForProcessing() {
        Pageable pageable = PageRequest.of(0, batchSize);
        return sagaRepo.findAllForProcessing(SagaStatus.RECEIVED, pageable);
    }

    public List<Saga> findAllFailedForProcessing() {
        Pageable pageable = PageRequest.of(0, batchSize);
        return sagaRepo.findAllForProcessing(SagaStatus.FAILED, pageable);
    }

    @Transactional
    public void markAllAs(List<Saga> sagas, SagaStatus status) {
        sagaRepo.updateAllStatus(sagas, status);
    }

    @Transactional
    public void markAllAsByIds(List<UUID> sagaIds, SagaStatus status) {
        sagaRepo.updateAllStatusByIds(sagaIds, status);
    }
}