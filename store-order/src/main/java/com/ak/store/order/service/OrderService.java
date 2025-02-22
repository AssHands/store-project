package com.ak.store.order.service;

import com.ak.store.common.model.order.dto.OrderDTO;
import com.ak.store.order.model.Order;
import com.ak.store.order.repository.OrderRepo;
import com.ak.store.order.util.OrderMapper;
import com.ak.store.order.validator.business.OrderBusinessValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final OrderBusinessValidator orderBusinessValidator;
    private final OrderMapper orderMapper;

    public List<Order> findAllByConsumerId(Long consumerId) {
        return orderRepo.findAllWithProductsByConsumerId(consumerId);
    }

    public void createOne(Long consumerId, OrderDTO orderDTO) {
        orderBusinessValidator.validateCreation(consumerId, orderDTO.getProductIds());
        Order
        orderRepo.save(orderMapper.mapToOrder(orderDTO));
    }
}
