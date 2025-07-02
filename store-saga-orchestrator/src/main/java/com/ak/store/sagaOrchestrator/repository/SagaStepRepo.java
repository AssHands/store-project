package com.ak.store.sagaOrchestrator.repository;

import com.ak.store.sagaOrchestrator.model.entity.SagaStep;
import com.ak.store.sagaOrchestrator.model.entity.SagaStepStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.time.LocalDateTime;
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
    @Query(nativeQuery = true, value = """
            INSERT INTO saga_step (name, is_compensation, status, saga_id, time)
            VALUES (:name, :isCompensation, :status, :sagaId, :time)
            ON CONFLICT (name, is_compensation, saga_id) DO NOTHING
            """)
    int saveOneIgnoreDuplicate(String name,
                               boolean isCompensation,
                               String status,
                               UUID sagaId,
                               LocalDateTime time);

    @Modifying
    @Query("UPDATE SagaStep ss SET ss.status = :status WHERE ss IN :sagaSteps")
    void updateAll(List<SagaStep> sagaSteps, SagaStepStatus status);
}