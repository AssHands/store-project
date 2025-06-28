package com.ak.store.paymentSagaWorker.sheduler;

import com.ak.store.paymentSagaWorker.inbox.InboxEvent;
import com.ak.store.paymentSagaWorker.inbox.InboxEventReaderService;
import com.ak.store.paymentSagaWorker.inbox.InboxEventType;
import com.ak.store.paymentSagaWorker.processor.InboxEventProcessor;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InboxEventScheduler {
    private final InboxEventReaderService inboxEventReaderService;
    private final Map<InboxEventType, InboxEventProcessor> eventProcessors;

    public InboxEventScheduler(InboxEventReaderService inboxEventReaderService, List<InboxEventProcessor> eventProcessors) {
        this.inboxEventReaderService = inboxEventReaderService;

        this.eventProcessors = eventProcessors.stream()
                .collect(Collectors.toMap(
                        InboxEventProcessor::getType,
                        processor -> processor
                ));
    }

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void executeInboxEvents() {
        for (var entry : eventProcessors.entrySet()) {
            processInboxEventsOfType(entry.getKey());
        }
    }

    private void processInboxEventsOfType(InboxEventType type) {
        var processor = eventProcessors.get(type);
        var events = inboxEventReaderService.findAllForProcessing(type);
        List<InboxEvent> completedEvents = new ArrayList<>();

        for (var event : events) {
            try {
                processor.process(event);
                completedEvents.add(event);
            } catch (Exception ignored) {
                //todo ловить, записывать в outbox топик, отправлять в кафку failed status
            }
        }

        if (!completedEvents.isEmpty()) {
            inboxEventReaderService.markAllAsCompleted(completedEvents);
        }
    }
}