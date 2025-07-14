package com.ak.store.emailSender.scheduler;

import com.ak.store.emailSender.inbox.InboxEvent;
import com.ak.store.emailSender.inbox.InboxEventType;
import com.ak.store.emailSender.processor.InboxEventProcessor;
import com.ak.store.emailSender.service.InboxEventReaderService;
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
    private final Map<InboxEventType, InboxEventProcessor> inboxEventProcessors;

    public InboxEventScheduler(InboxEventReaderService inboxEventReaderService, List<InboxEventProcessor> inboxEventProcessors) {
        this.inboxEventReaderService = inboxEventReaderService;

        this.inboxEventProcessors = inboxEventProcessors.stream()
                .collect(Collectors.toMap(
                        InboxEventProcessor::getType,
                        processor -> processor
                ));
    }

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void executeInboxEvents() {
        for (var entry : inboxEventProcessors.entrySet()) {
            processInboxEventsOfType(entry.getKey());
        }
    }

    private void processInboxEventsOfType(InboxEventType type) {
        var processor = inboxEventProcessors.get(type);
        var events = inboxEventReaderService.findAllForProcessing(type);
        List<InboxEvent> completedEvents = new ArrayList<>();

        for (var event : events) {
            try {
                processor.process(event);
                completedEvents.add(event);
            } catch (Exception ignored) {
            }
        }

        if(!completedEvents.isEmpty()) {
            inboxEventReaderService.markAllAsCompleted(completedEvents);
        }
    }
}