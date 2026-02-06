package com.ak.store.paymentSagaWorker.processor.inbox.impl;

import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.snapshot.order.OrderCreatedSnapshot;
import com.ak.store.paymentSagaWorker.model.inbox.InboxEvent;
import com.ak.store.paymentSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.paymentSagaWorker.model.inbox.InboxEventType;
import com.ak.store.paymentSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.paymentSagaWorker.service.InboxEventReaderService;
import com.ak.store.paymentSagaWorker.service.UserBalanceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReserveFundsInboxEventProcessor implements InboxEventProcessor {
    private final UserBalanceService userBalanceService;
    private final JsonMapperKafka jsonMapperKafka;
    private final InboxEventReaderService inboxEventReaderService;

    @Transactional
    @Override
    public void process(InboxEvent event) {
        var request = jsonMapperKafka.fromJson(event.getPayload(), OrderCreatedSnapshot.class);

        try {
            userBalanceService.reserveFunds(request.getUserId(), request.getTotalPrice());
            inboxEventReaderService.markOneAs(event, InboxEventStatus.SUCCESS);
        } catch (Exception e) {
            inboxEventReaderService.markOneAsFailure(event);
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.RESERVE_FUNDS;
    }
}
