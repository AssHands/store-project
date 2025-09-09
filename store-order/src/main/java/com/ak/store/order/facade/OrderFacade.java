package com.ak.store.order.facade;

import com.ak.store.order.model.dto.OrderDTOPayload;
import com.ak.store.order.model.dto.UserAuthContext;
import com.ak.store.order.outbox.OutboxEventService;
import com.ak.store.order.outbox.OutboxEventType;
import com.ak.store.order.service.OrderService;
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
    private final OutboxEventService outboxEventService;

    public List<OrderDTOPayload> findAllByUserId(UUID userId) {
        return orderService.findAllByUserId(userId);
    }

    @Transactional
    public void createOne(UserAuthContext authContext, Map<Long, Integer> productMap) {
        var orderPayload = orderService.createOne(authContext.getId(), productMap);

        Map<Long, Integer> productAmount = new HashMap<>();
        for (var product : orderPayload.getProducts()) {
            productAmount.merge(product.getProductId(), 1, Integer::sum);
        }

        var snapshot = OrderCreationSnapshot.builder()
                .orderId(orderPayload.getOrder().getId())
                .userId(authContext.getId())
                .totalPrice(orderPayload.getOrder().getTotalPrice())
                .productAmount(productAmount)
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.ORDER_CREATION);
    }
}