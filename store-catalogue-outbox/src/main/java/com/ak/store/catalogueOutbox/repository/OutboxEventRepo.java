package com.ak.store.catalogueOutbox.repository;

import com.ak.store.catalogueOutbox.model.OutboxEvent;
import com.ak.store.catalogueOutbox.model.OutboxEventStatus;
import com.ak.store.catalogueOutbox.model.OutboxEventType;
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
public interface OutboxEventRepo extends JpaRepository<OutboxEvent, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT e FROM OutboxEvent e
            WHERE e.type = :type
            AND e.retryTime <= :retryTime
            AND e.status = :status
            ORDER BY e.retryTime ASC
            """)
    List<OutboxEvent> findAllForProcessing(OutboxEventType type, OutboxEventStatus status,
                                           LocalDateTime retryTime, Pageable pageable);

    @Modifying
    @Query("UPDATE OutboxEvent e SET e.status = :status WHERE e IN :events")
    void updateAll(List<OutboxEvent> events, OutboxEventStatus status);

    @Modifying
    @Query("UPDATE OutboxEvent e SET e.status = :status WHERE e = :event")
    void updateOne(OutboxEvent event, OutboxEventStatus status);
}