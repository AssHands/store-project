package com.ak.store.catalogue.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class OutboxTaskService<T> {
    private final OutboxTaskRepo outboxTaskRepo;
    private final OutboxTaskMapper<T> outboxTaskMapper;

    public void createOneTask(T payload, OutboxTaskType type) {
        var task = outboxTaskMapper.mapToOutboxTask(payload);

        task.setType(type);
        task.setStatus(OutboxTaskStatus.IN_PROGRESS);
        task.setRetryTime(LocalDateTime.now());

        outboxTaskRepo.save(task);
    }
}