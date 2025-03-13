package com.ak.store.outboxScheduler.service;

import com.ak.store.outboxScheduler.model.OutboxTask;
import com.ak.store.outboxScheduler.model.OutboxTaskStatus;
import com.ak.store.outboxScheduler.model.OutboxTaskType;
import com.ak.store.outboxScheduler.repo.OutboxTaskRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OutboxTaskService {
    private final OutboxTaskRepo outboxTaskRepo;
    private Integer batchSize = 100;
    private Integer retryTimeMinutes = 5;

    @Transactional
    public List<OutboxTask> getAllTaskForProcessing(OutboxTaskType type) {
        LocalDateTime retryTime = LocalDateTime.now();

        Pageable pageable = PageRequest.of(0, batchSize);
        List<OutboxTask> tasks = outboxTaskRepo.findAllTaskForProcessing(
                type, OutboxTaskStatus.IN_PROGRESS, LocalDateTime.now(), pageable);

        for (OutboxTask task : tasks) {
            task.setRetryTime(retryTime.plusMinutes(retryTimeMinutes));
        }

        return tasks;
    }

    @Transactional
    public List<OutboxTask> getAllTaskForProcessingAndMarkAsCompleted(OutboxTaskType type) {
        LocalDateTime retryTime = LocalDateTime.now();

        Pageable pageable = PageRequest.of(0, batchSize);
        List<OutboxTask> tasks = outboxTaskRepo.findAllTaskForProcessing(
                type, OutboxTaskStatus.IN_PROGRESS, LocalDateTime.now(), pageable);

        for (OutboxTask task : tasks) {
            task.setRetryTime(retryTime.plusMinutes(retryTimeMinutes));
            task.setStatus(OutboxTaskStatus.COMPLETED);
        }

        return tasks;
    }

    @Transactional
    public void markAllTaskAsCompleted(List<OutboxTask> tasks) {
        outboxTaskRepo.updateAllTask(tasks, OutboxTaskStatus.COMPLETED);
    }
}
