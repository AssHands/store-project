package com.ak.store.orderSagaWorker.repository;

import com.ak.store.orderSagaWorker.model.entity.InboxEvent;
import com.ak.store.orderSagaWorker.model.entity.InboxEventStatus;
import com.ak.store.orderSagaWorker.model.entity.InboxEventType;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface InboxEventRepo extends JpaRepository<InboxEvent, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT e FROM InboxEvent e
            WHERE e.type = :type
            AND e.retryTime <= :retryTime
            AND e.status IN :statuses
            ORDER BY e.retryTime ASC
            """)
    List<InboxEvent> findAllForProcessing(InboxEventType type, List<InboxEventStatus> statuses,
                                          LocalDateTime retryTime, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = """
            INSERT INTO inbox (id, step_name, payload, type, status, retry_time)
            VALUES (:id, :stepName, CAST(:payload AS jsonb), :type, :status, :retryTime)
            ON CONFLICT (id) DO NOTHING
            """)
    int saveOneIgnoreDuplicate(UUID id,
                               String stepName,
                               String payload,
                               String type,
                               String status,
                               LocalDateTime retryTime);

    @Modifying
    @Query(nativeQuery = true, value = """
            INSERT INTO inbox (id, step_name, type, status, retry_time)
            VALUES (:id, :stepName, :type, :status, :retryTime)
            ON CONFLICT (id) DO NOTHING
            """)
    int saveOneIgnoreDuplicate(UUID id,
                               String stepName,
                               String type,
                               String status,
                               LocalDateTime retryTime);

    @Modifying
    @Query("UPDATE InboxEvent e SET e.status = :status, e.retryTime = :time WHERE e IN :events")
    void updateAll(List<InboxEvent> events, InboxEventStatus status, LocalDateTime time);
}