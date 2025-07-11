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
            AND ss.retryTime <= :retryTime
            ORDER BY ss.time ASC
            """)
    List<SagaStep> findAllForProcessing(SagaStepStatus status, LocalDateTime retryTime, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = """
            INSERT INTO saga_step (name, is_compensation, status, saga_id, time, retry_time)
            VALUES (:name, :isCompensation, :status, :sagaId, :time, :retryTime)
            ON CONFLICT (name, is_compensation, saga_id) DO NOTHING
            """)
    int saveOneIgnoreDuplicate(UUID sagaId,
                               String name,
                               boolean isCompensation,
                               String status,
                               LocalDateTime time,
                               LocalDateTime retryTime);

    @Modifying
    @Query("UPDATE SagaStep ss SET ss.status = :status, ss.retryTime = :retryTime WHERE ss.id = :id")
    void updateOneStatusById(UUID id, SagaStepStatus status, LocalDateTime retryTime);


    @Modifying
    @Query("UPDATE SagaStep ss SET ss.status = :status, ss.retryTime = :retryTime WHERE ss IN :sagaSteps")
    void updateAllStatus(List<SagaStep> sagaSteps, SagaStepStatus status, LocalDateTime retryTime);
}