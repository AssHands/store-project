package com.ak.store.reviewSagaWorker.scheduler;

import com.ak.store.reviewSagaWorker.model.inbox.InboxEventType;
import com.ak.store.reviewSagaWorker.processor.outbox.OutboxEventProcessor;
import com.ak.store.reviewSagaWorker.service.InboxEventReaderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OutboxEventScheduler {
    private final InboxEventReaderService inboxEventReaderService;
    private final Map<InboxEventType, OutboxEventProcessor> outboxEventProcessors;

    public OutboxEventScheduler(InboxEventReaderService inboxEventReaderService, List<OutboxEventProcessor> outboxEventProcessors) {
        this.inboxEventReaderService = inboxEventReaderService;
        this.outboxEventProcessors = outboxEventProcessors.stream()
                .collect(Collectors.toMap(
                        OutboxEventProcessor::getType,
                        processor -> processor
                ));
    }

    @Scheduled(fixedRate = 5000)
    public void executeCompletedInboxEvents() {
        for (var entry : outboxEventProcessors.entrySet()) {
            processCompletedInboxEventsOfType(entry.getKey());
        }
    }

    private void processCompletedInboxEventsOfType(InboxEventType type) {
        var processor = outboxEventProcessors.get(type);
        var events = inboxEventReaderService.findAllCompletedForProcessing(type);

        for (var event : events) {
            processor.process(event);
        }
    }
}