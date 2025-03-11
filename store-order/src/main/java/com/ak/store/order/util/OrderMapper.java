package com.ak.store.order.util;

import com.ak.store.common.model.order.dto.OrderDTO;
import com.ak.store.common.model.order.view.OrderView;
import com.ak.store.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final ModelMapper modelMapper;

    public OrderView mapToOrderView(Order order) {
        return modelMapper.map(order, OrderView.class);
    }

    public Order mapToOrder(OrderDTO orderDTO) {
        var order = modelMapper.map(orderDTO, Order.class);
        for (var orderProduct : order.getProducts()) {
            orderProduct.setId(null);
        }

        return order;
    }
}
