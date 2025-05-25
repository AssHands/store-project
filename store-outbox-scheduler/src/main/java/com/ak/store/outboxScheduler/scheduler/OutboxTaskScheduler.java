package com.ak.store.outboxScheduler.scheduler;

import com.ak.store.outboxScheduler.model.OutboxTask;
import com.ak.store.outboxScheduler.model.OutboxTaskType;
import com.ak.store.outboxScheduler.processor.OutboxTaskProcessor;
import com.ak.store.outboxScheduler.service.OutboxTaskService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OutboxTaskScheduler {
    private final OutboxTaskService outboxTaskService;
    private final Map<OutboxTaskType, OutboxTaskProcessor> taskProcessors;

    public OutboxTaskScheduler(OutboxTaskService outboxTaskService, List<OutboxTaskProcessor> taskProcessors) {
        this.outboxTaskService = outboxTaskService;

        this.taskProcessors = taskProcessors.stream()
                .collect(Collectors.toMap(
                        OutboxTaskProcessor::getType,
                        processor -> processor
                ));
    }

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void executeOutboxTasks() {
        for (var entry : taskProcessors.entrySet()) {
            processOutboxTasksOfType(entry.getKey());
        }
    }

    private void processOutboxTasksOfType(OutboxTaskType type) {
        var processor = taskProcessors.get(type);
        var tasks = outboxTaskService.getAllTaskForProcessing(type);
        List<OutboxTask> completedTasks = new ArrayList<>();

        //todo make async

//        processor.sendAsync(task)
//                .thenRun(() -> outboxTaskService.markAsCompleted(task))
//                .exceptionally(ex -> {
//                    log.error("Failed to process task {}", task.getId(), ex);
//                    return null;
//                });

        for (var task : tasks) {
            try {
                processor.process(task);
                completedTasks.add(task);
            } catch (Exception ignored) {}
        }

        outboxTaskService.markAllAsCompleted(completedTasks);
    }
}