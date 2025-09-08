package com.ak.store.reviewOutbox.scheduler;

import com.ak.store.reviewOutbox.model.OutboxEventType;
import com.ak.store.reviewOutbox.processor.OutboxEventProcessor;
import com.ak.store.reviewOutbox.service.OutboxEventService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OutboxEventScheduler {
    private final OutboxEventService outboxEventService;
    private final Map<OutboxEventType, OutboxEventProcessor> eventProcessors;

    public OutboxEventScheduler(OutboxEventService outboxEventService, List<OutboxEventProcessor> eventProcessors) {
        this.outboxEventService = outboxEventService;

        this.eventProcessors = eventProcessors.stream()
                .collect(Collectors.toMap(
                        OutboxEventProcessor::getType,
                        processor -> processor
                ));
    }

    @Scheduled(fixedRate = 5000)
    public void executeOutboxEvents() {
        for (var entry : eventProcessors.entrySet()) {
            processOutboxEventsOfType(entry.getKey());
        }
    }

    private void processOutboxEventsOfType(OutboxEventType type) {
        var events = outboxEventService.findAllForProcessing(type);
        var processor = eventProcessors.get(type);

        for (var event : events) {
            processor.process(event);
        }
    }
}