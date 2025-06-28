package com.ak.store.paymentSagaWorker.processor.impl;

import com.ak.store.paymentSagaWorker.inbox.InboxEvent;
import com.ak.store.paymentSagaWorker.inbox.InboxEventType;
import com.ak.store.paymentSagaWorker.processor.InboxEventProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReleaseFundsInboxEventProcessor implements InboxEventProcessor {

    @Override
    public void process(InboxEvent event) {

    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.RELEASE_FUNDS;
    }
}
