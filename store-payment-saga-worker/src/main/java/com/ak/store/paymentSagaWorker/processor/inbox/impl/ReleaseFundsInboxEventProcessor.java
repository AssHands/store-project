package com.ak.store.paymentSagaWorker.processor.inbox.impl;

import com.ak.store.paymentSagaWorker.model.entity.InboxEvent;
import com.ak.store.paymentSagaWorker.model.entity.InboxEventType;
import com.ak.store.paymentSagaWorker.model.dto.ReserveFundsSagaRequestEvent;
import com.ak.store.paymentSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.paymentSagaWorker.service.UserBalanceService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReleaseFundsInboxEventProcessor implements InboxEventProcessor {
    private final UserBalanceService userBalanceService;
    private final Gson gson;

    @Override
    public void process(InboxEvent event) {
        var reserveFundsEvent = gson.fromJson(event.getPayload(), ReserveFundsSagaRequestEvent.class);
        userBalanceService.releaseFunds(reserveFundsEvent.getUserId(), reserveFundsEvent.getTotalPrice());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.RELEASE_FUNDS;
    }
}
