package com.ak.store.paymentSagaWorker.scheduler;

import com.ak.store.paymentSagaWorker.model.entity.InboxEvent;
import com.ak.store.paymentSagaWorker.model.entity.InboxEventStatus;
import com.ak.store.paymentSagaWorker.model.entity.InboxEventType;
import com.ak.store.paymentSagaWorker.processor.outbox.OutboxEventProcessor;
import com.ak.store.paymentSagaWorker.service.InboxEventReaderService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void executeCompletedInboxEvents() {
        for (var entry : outboxEventProcessors.entrySet()) {
            processCompletedInboxEventsOfType(entry.getKey());
        }
    }

    private void processCompletedInboxEventsOfType(InboxEventType type) {
        var processor = outboxEventProcessors.get(type);
        var events = inboxEventReaderService.findAllCompletedForProcessing(type);
        List<InboxEvent> completedEvents = new ArrayList<>();

        for (var event : events) {
            try {
                processor.process(event);
                completedEvents.add(event);
            } catch (Exception ignored) {}
        }

        if (!completedEvents.isEmpty()) {
            inboxEventReaderService.markAllAs(completedEvents, InboxEventStatus.COMPLETED);
        }
    }
}