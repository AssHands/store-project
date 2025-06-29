package com.ak.store.orderSagaWorker.processor.inbox.impl;

import com.ak.store.orderSagaWorker.model.dto.OrderConfirmSagaRequestEvent;
import com.ak.store.orderSagaWorker.model.entity.InboxEvent;
import com.ak.store.orderSagaWorker.model.entity.InboxEventType;
import com.ak.store.orderSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.orderSagaWorker.service.OrderService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelOrderInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final OrderService orderService;

    @Override
    public void process(InboxEvent event) {
        var orderCanselEvent = gson.fromJson(event.getPayload(), OrderConfirmSagaRequestEvent.class);
        orderService.cancelOne(orderCanselEvent.getOrderId());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_ORDER;
    }
}
