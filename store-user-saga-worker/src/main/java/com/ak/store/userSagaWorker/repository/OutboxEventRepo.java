package com.ak.store.userSagaWorker.repository;

import com.ak.store.userSagaWorker.model.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface OutboxEventRepo extends JpaRepository<OutboxEvent, UUID> {
    @Modifying
    @Query(nativeQuery = true, value = """
            INSERT INTO outbox (id, payload, type, status, retry_time)
            VALUES (:id, CAST(:payload AS jsonb), :type, :status, :retryTime)
            ON CONFLICT (id) DO NOTHING
            """)
    int saveOneIgnoreDuplicate(UUID id,
                               String payload,
                               String type,
                               String status,
                               LocalDateTime retryTime);
}