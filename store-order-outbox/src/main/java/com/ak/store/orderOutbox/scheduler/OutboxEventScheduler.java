package com.ak.store.orderOutbox.scheduler;

import com.ak.store.orderOutbox.model.OutboxEvent;
import com.ak.store.orderOutbox.model.OutboxEventType;
import com.ak.store.orderOutbox.processor.OutboxEventProcessor;
import com.ak.store.orderOutbox.service.OutboxEventService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void executeOutboxEvents() {
        for (var entry : eventProcessors.entrySet()) {
            processOutboxEventsOfType(entry.getKey());
        }
    }

    private void processOutboxEventsOfType(OutboxEventType type) {
        var processor = eventProcessors.get(type);
        var events = outboxEventService.findAllForProcessing(type);
        List<OutboxEvent> completedEvents = new ArrayList<>();

        for (var event : events) {
            try {
                processor.process(event);
                completedEvents.add(event);
            } catch (Exception ignored) {
            }
        }

        outboxEventService.markAllAsCompleted(completedEvents);
    }
}