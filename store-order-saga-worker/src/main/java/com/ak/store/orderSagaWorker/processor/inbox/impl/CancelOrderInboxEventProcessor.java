package com.ak.store.orderSagaWorker.processor.inbox.impl;

import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.snapshot.order.OrderCreation;
import com.ak.store.orderSagaWorker.model.inbox.InboxEvent;
import com.ak.store.orderSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.orderSagaWorker.model.inbox.InboxEventType;
import com.ak.store.orderSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.orderSagaWorker.service.InboxEventReaderService;
import com.ak.store.orderSagaWorker.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelOrderInboxEventProcessor implements InboxEventProcessor {
    private final JsonMapperKafka jsonMapperKafka;
    private final OrderService orderService;
    private final InboxEventReaderService inboxEventReaderService;

    @Transactional
    @Override
    public void process(InboxEvent event) {
        var request = jsonMapperKafka.fromJson(event.getPayload(), OrderCreation.class);

        try {
            orderService.cancelOne(request.getOrderId());
            inboxEventReaderService.markOneAs(event, InboxEventStatus.SUCCESS);
        } catch (Exception e) {
            //todo сделать логику retry. сейчас в случае неудачи - событие сразу помечается как неудачное
            inboxEventReaderService.markOneAsFailure(event);
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_ORDER;
    }
}
