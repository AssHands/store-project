package com.ak.store.orderSagaWorker.service;

import com.ak.store.orderSagaWorker.model.entity.Order;
import com.ak.store.orderSagaWorker.model.entity.OrderStatus;
import com.ak.store.orderSagaWorker.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepo orderRepo;

    private Order findOneById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public void confirmOne(Long id) {
        var order = findOneById(id);
        order.setStatus(OrderStatus.COMPLETED);
    }

    public void cancelOne(Long id) {
        var order = findOneById(id);
        order.setStatus(OrderStatus.FAILED);
    }
}
