package com.ak.store.order.facade;

import com.ak.store.common.event.order.OrderCreatedEvent;
import com.ak.store.common.model.order.dto.OrderDTO;
import com.ak.store.common.model.order.dto.ProductAmountDTO;
import com.ak.store.common.model.order.view.OrderView;
import com.ak.store.order.feign.WarehouseFeign;
import com.ak.store.order.kafka.OrderProducerKafka;
import com.ak.store.order.model.Order;
import com.ak.store.order.service.OrderService;
import com.ak.store.order.util.mapper.OrderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final WarehouseFeign warehouseFeign;
    private final OrderProducerKafka orderProducerKafka;

    public List<OrderView> findAllByConsumerId(String consumerId) {
        return orderService.findAllByConsumerId(consumerId).stream()
                .map(orderMapper::toOrderView)
                .toList();
    }

    @Transactional
    public void createOne(Jwt accessToken, OrderDTO orderDTO) {
        Order order = orderService.createOne(accessToken.getSubject(), orderDTO);

        List<ProductAmountDTO> orderProductList = new ArrayList<>();
        for (var orderProduct : orderDTO.getProducts()) {
            orderProductList.add(new ProductAmountDTO(
                    orderProduct.getProductId(), orderProduct.getAmount()));
        }

        warehouseFeign.reserveAll(orderProductList);

        orderProducerKafka.send(OrderCreatedEvent.builder()
                .orderId(order.getId())
                .consumerEmail(accessToken.getClaimAsString("email"))
                .orderProducts(orderProductList)
                .totalPrice(order.getTotalPrice())
                .build());
    }
}