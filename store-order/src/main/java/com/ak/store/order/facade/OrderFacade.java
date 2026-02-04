package com.ak.store.order.facade;

import com.ak.store.order.model.command.WriteOrderCommand;
import com.ak.store.order.model.dto.OrderPayloadDTO;
import com.ak.store.order.outbox.OutboxEventService;
import com.ak.store.order.outbox.OutboxEventType;
import com.ak.store.order.service.OrderService;
import com.ak.store.order.service.outbox.OrderOutboxService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderFacade {
    private final OrderService orderService;
    private final OrderOutboxService orderOutboxService;

    public List<OrderPayloadDTO> findAllByUserId(UUID userId) {
        return orderService.findAllByUserId(userId);
    }

    @Transactional
    public void createOne(WriteOrderCommand command) {
        //todo объединить order и orderPayload
        var orderPayload = orderService.createOne(command);
        orderOutboxService.saveCreationEvent(orderPayload.getOrder().getId());
    }
}