package com.ak.store.outboxScheduler.scheduler;

import com.ak.store.outboxScheduler.model.OutboxTaskType;
import com.ak.store.outboxScheduler.service.OutboxTaskService;
import com.ak.store.outboxScheduler.processor.OutboxTaskProcessor;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
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
            var taskType = entry.getKey();
            var taskProcessor = entry.getValue();

            var tasks = outboxTaskService.getAllTaskForProcessing(taskType);
            taskProcessor.process(tasks);
        }
    }
}
