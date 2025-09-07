package com.ak.store.paymentSagaWorker.processor.inbox.impl;

import com.ak.store.paymentSagaWorker.model.dto.UserPaymentCreationSagaRequestEvent;
import com.ak.store.paymentSagaWorker.model.inbox.InboxEvent;
import com.ak.store.paymentSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.paymentSagaWorker.model.inbox.InboxEventType;
import com.ak.store.paymentSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.paymentSagaWorker.service.InboxEventReaderService;
import com.ak.store.paymentSagaWorker.service.UserBalanceService;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserPaymentCreationInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final UserBalanceService userBalanceService;
    private final InboxEventReaderService inboxEventReaderService;

    @Transactional
    @Override
    public void process(InboxEvent event) {
        var userPaymentCreationRequest = gson.fromJson(event.getPayload(), UserPaymentCreationSagaRequestEvent.class);

        try {
            userBalanceService.createOne(userPaymentCreationRequest.getUserId());
            inboxEventReaderService.markOneAs(event, InboxEventStatus.SUCCESS);
        } catch (Exception e) {
            inboxEventReaderService.markOneAsFailure(event);
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.USER_PAYMENT_CREATION;
    }
}
