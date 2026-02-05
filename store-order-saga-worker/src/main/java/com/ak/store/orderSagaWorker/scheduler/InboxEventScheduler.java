package com.ak.store.orderSagaWorker.scheduler;

import com.ak.store.orderSagaWorker.model.inbox.InboxEventType;
import com.ak.store.orderSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.orderSagaWorker.service.InboxEventReaderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InboxEventScheduler {
    private final InboxEventReaderService inboxEventReaderService;
    private final Map<InboxEventType, InboxEventProcessor> inboxEventProcessors;

    public InboxEventScheduler(InboxEventReaderService inboxEventReaderService, List<InboxEventProcessor> inboxEventProcessors) {
        this.inboxEventReaderService = inboxEventReaderService;

        this.inboxEventProcessors = inboxEventProcessors.stream()
                .collect(Collectors.toMap(
                        InboxEventProcessor::getType,
                        processor -> processor
                ));
    }

    @Scheduled(fixedRate = 5000)
    public void executeInboxEvents() {
        for (var entry : inboxEventProcessors.entrySet()) {
            processInboxEventsOfType(entry.getKey());
        }
    }

    private void processInboxEventsOfType(InboxEventType type) {
        var processor = inboxEventProcessors.get(type);
        var events = inboxEventReaderService.findAllForProcessing(type);

        for (var event : events) {
            processor.process(event);
        }
    }
}