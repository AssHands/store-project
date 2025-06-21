package com.ak.store.sagaOrchestrator.repository;

import com.ak.store.sagaOrchestrator.model.entity.SagaStep;
import com.ak.store.sagaOrchestrator.model.entity.SagaStepStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.UUID;

public interface SagaStepRepo extends JpaRepository<SagaStep, UUID> {
    @EntityGraph(attributePaths = "saga")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT ss FROM SagaStep ss
            WHERE ss.status = :status
            ORDER BY ss.time ASC
            """)
    List<SagaStep> findAllForProcessing(SagaStepStatus status, Pageable pageable);

    @Modifying
    @Query("UPDATE SagaStep ss SET ss.status = :status WHERE ss IN :sagaSteps")
    void updateAll(List<SagaStep> sagaSteps, SagaStepStatus status);
}