package com.ak.store.sagaOrchestrator.repository;

import com.ak.store.sagaOrchestrator.model.entity.Saga;
import com.ak.store.sagaOrchestrator.model.entity.SagaStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SagaRepo extends JpaRepository<Saga, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT s FROM Saga s
            WHERE s.status = :status
            ORDER BY s.time ASC
            """)
    List<Saga> findAllForProcessing(SagaStatus status, Pageable pageable);

    @Modifying
    @Query("UPDATE Saga s SET s.status = :status WHERE s IN :sagas")
    void updateAllStatus(List<Saga> sagas, SagaStatus status);

    @Modifying
    @Query("UPDATE Saga s SET s.status = :status WHERE s.id IN :sagaIds")
    void updateAllStatusByIds(List<UUID> sagaIds, SagaStatus status);
}