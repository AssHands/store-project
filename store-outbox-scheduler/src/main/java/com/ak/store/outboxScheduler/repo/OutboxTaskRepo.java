package com.ak.store.outboxScheduler.repo;

import com.ak.store.outboxScheduler.model.OutboxTask;
import com.ak.store.outboxScheduler.model.OutboxTaskStatus;
import com.ak.store.outboxScheduler.model.OutboxTaskType;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutboxTaskRepo extends JpaRepository<OutboxTask, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT t FROM OutboxTask t
            WHERE t.type = :type
            AND t.retryTime <= :retryTime
            AND t.status = :status
            ORDER BY t.retryTime ASC
            """)
    List<OutboxTask> findAllTaskForProcessing(OutboxTaskType type, OutboxTaskStatus status,
                                              LocalDateTime retryTime, Pageable pageable);

    @Modifying
    @Query("UPDATE OutboxTask t SET t.status = :status WHERE t IN :tasks")
    void updateAllTask(List<OutboxTask> tasks, OutboxTaskStatus status);
}
