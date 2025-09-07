package com.ak.store.orderSagaWorker.service;

import com.ak.store.orderSagaWorker.model.order.Order;
import com.ak.store.orderSagaWorker.model.order.OrderStatus;
import com.ak.store.orderSagaWorker.repository.OrderRepo;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void confirmOne(Long id) {
        var order = findOneById(id);
        order.setStatus(OrderStatus.COMPLETED);
    }

    @Transactional
    public void cancelOne(Long id) {
        var order = findOneById(id);
        order.setStatus(OrderStatus.FAILED);
    }
}
