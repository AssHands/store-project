package com.ak.store.paymentSagaWorker.processor.inbox.impl;

import com.ak.store.kafka.storekafkastarter.util.JsonMapperKafka;
import com.ak.store.paymentSagaWorker.model.dto.CancelUserPaymentCreationSagaRequestEvent;
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
public class CancelUserPaymentCreationInboxEventProcessor implements InboxEventProcessor {
    private final JsonMapperKafka jsonMapperKafka;
    private final InboxEventReaderService inboxEventReaderService;
    private final UserBalanceService userBalanceService;

    @Transactional
    @Override
    public void process(InboxEvent event) {
        var cancelUserPaymentCreationRequest = jsonMapperKafka.fromJson(event.getPayload(), CancelUserPaymentCreationSagaRequestEvent.class);

        try {
            userBalanceService.deleteOne(cancelUserPaymentCreationRequest.getUserId());
            inboxEventReaderService.markOneAs(event, InboxEventStatus.SUCCESS);
        } catch (Exception e) {
            inboxEventReaderService.markOneAsFailure(event);
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_USER_PAYMENT_CREATION;
    }
}
