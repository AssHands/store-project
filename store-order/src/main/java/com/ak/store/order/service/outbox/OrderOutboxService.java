package com.ak.store.order.service.outbox;

import com.ak.store.kafka.storekafkastarter.model.snapshot.order.OrderCreationSnapshot;
import com.ak.store.order.model.entity.Order;
import com.ak.store.order.outbox.OutboxEventService;
import com.ak.store.order.outbox.OutboxEventType;
import com.ak.store.order.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderOutboxService {
    private final OrderRepo orderRepo;
    private final OutboxEventService outboxEventService;

    public void saveCreationEvent(Long id) {
        var order = findOne(id);

        Map<Long, Integer> productAmount = new HashMap<>();
        for (var product : order.getProducts()) {
            productAmount.merge(product.getProductId(), 1, Integer::sum);
        }

        var snapshot = OrderCreationSnapshot.builder()
                .userId(order.getUserId())
                .orderId(order.getId())
                .totalPrice(order.getTotalPrice())
                .productAmount(productAmount)
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.ORDER_CREATION);
    }

    private Order findOne(Long id) {
        return orderRepo.findOneFullById(id)
                .orElseThrow(() -> new RuntimeException("order not found"));
    }
}
