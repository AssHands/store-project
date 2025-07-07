package com.ak.store.paymentSagaWorker.processor.inbox.impl;

import com.ak.store.paymentSagaWorker.model.dto.UserPaymentCreationSagaRequestEvent;
import com.ak.store.paymentSagaWorker.model.entity.InboxEvent;
import com.ak.store.paymentSagaWorker.model.entity.InboxEventType;
import com.ak.store.paymentSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.paymentSagaWorker.service.UserBalanceService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserPaymentCreationInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final UserBalanceService userBalanceService;

    @Override
    public void process(InboxEvent event) {
        var userPaymentCreationRequest = gson.fromJson(event.getPayload(), UserPaymentCreationSagaRequestEvent.class);
        userBalanceService.createOne(userPaymentCreationRequest.getUserId());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.USER_PAYMENT_CREATION;
    }
}
