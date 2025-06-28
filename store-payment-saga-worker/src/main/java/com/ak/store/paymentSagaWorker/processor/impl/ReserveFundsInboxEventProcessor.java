package com.ak.store.paymentSagaWorker.processor.impl;

import com.ak.store.paymentSagaWorker.inbox.InboxEvent;
import com.ak.store.paymentSagaWorker.inbox.InboxEventType;
import com.ak.store.paymentSagaWorker.model.dto.ReserveFundsSagaRequestEvent;
import com.ak.store.paymentSagaWorker.processor.InboxEventProcessor;
import com.ak.store.paymentSagaWorker.service.UserBalanceService;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReserveFundsInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final UserBalanceService userBalanceService;

    @Override
    @Transactional
    public void process(InboxEvent event) {
         var reserveFundsEvent = gson.fromJson(event.getPayload(), ReserveFundsSagaRequestEvent.class);
         userBalanceService.reserveFunds(reserveFundsEvent.getUserId(), reserveFundsEvent.getTotalPrice());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.RESERVE_FUNDS;
    }
}
