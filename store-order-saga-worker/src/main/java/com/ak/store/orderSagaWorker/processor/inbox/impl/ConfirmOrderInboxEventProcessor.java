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
public class ConfirmOrderInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final OrderService orderService;

    @Override
    public void process(InboxEvent event) {
        var orderConfirmEvent = gson.fromJson(event.getPayload(), OrderConfirmSagaRequestEvent.class);
        orderService.confirmOne(orderConfirmEvent.getOrderId());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CONFIRM_ORDER;
    }
}