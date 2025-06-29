package com.ak.store.warehouseSagaWorker.scheduler;

import com.ak.store.warehouseSagaWorker.model.entity.InboxEvent;
import com.ak.store.warehouseSagaWorker.model.entity.InboxEventStatus;
import com.ak.store.warehouseSagaWorker.model.entity.InboxEventType;
import com.ak.store.warehouseSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.warehouseSagaWorker.service.InboxEventReaderService;
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
        List<InboxEvent> successEvents = new ArrayList<>();
        List<InboxEvent> failedEvents = new ArrayList<>();

        for (var event : events) {
            try {
                processor.process(event);
                successEvents.add(event);
            } catch (Exception ignored) {
                //todo сделать логику retry. сейчас в случае неудачи - событие сразу помечается как неудачное
                failedEvents.add(event);
            }
        }

        if (!successEvents.isEmpty()) {
            inboxEventReaderService.markAllAs(successEvents, InboxEventStatus.SUCCESS);
        }
        if (!failedEvents.isEmpty()) {
            inboxEventReaderService.markAllAs(failedEvents, InboxEventStatus.FAILURE);
        }
    }
}