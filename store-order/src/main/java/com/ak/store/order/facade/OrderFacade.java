package com.ak.store.order.facade;

import com.ak.store.common.event.order.OrderCreatedEvent;
import com.ak.store.common.model.order.form.OrderForm;
import com.ak.store.common.model.order.dto.ProductAmount;
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
    public void createOne(Jwt accessToken, OrderForm orderForm) {
        Order order = orderService.createOne(accessToken.getSubject(), orderForm);

        List<ProductAmount> orderProductList = new ArrayList<>();
        for (var orderProduct : orderForm.getProducts()) {
            orderProductList.add(new ProductAmount(
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