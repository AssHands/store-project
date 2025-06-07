package com.ak.store.emailSender.processor.impl;

import com.ak.store.common.event.order.OrderCreatedEvent;
import com.ak.store.common.snapshot.order.OrderCreatedSnapshotPayload;
import com.ak.store.emailSender.facade.EmailFacade;
import com.ak.store.emailSender.inbox.InboxEvent;
import com.ak.store.emailSender.inbox.InboxEventType;
import com.ak.store.emailSender.mapper.EmailMapper;
import com.ak.store.emailSender.processor.InboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderCreatedInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final EmailFacade emailFacade;
    private final EmailMapper emailMapper;

    @Override
    public void process(InboxEvent event) {
        var orderCreatedEvent = new OrderCreatedEvent(event.getId(),
                gson.fromJson(event.getPayload(), OrderCreatedSnapshotPayload.class));

        emailFacade.sendOrderCreated(emailMapper.toOrderCreatedWriteDTO(orderCreatedEvent.getPayload()));
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.ORDER_CREATED;
    }
}