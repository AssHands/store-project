package com.ak.store.order.util;

import com.ak.store.common.model.catalogue.view.ProductPoorView;
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
        var orderView = modelMapper.map(order, OrderView.class);
        for (int i = 0; i < order.getProducts().size(); i++) {
            orderView.getProducts().get(i).setProductPoorView(ProductPoorView.builder()
                    .id(order.getProducts().get(i).getProductId())
                    .build());
        }

        return orderView;
    }

    public Order mapToOrder(OrderDTO orderDTO) {
        return
    }
}
