package com.ak.store.order.facade;

import com.ak.store.common.model.catalogue.view.ProductPoorView;
import com.ak.store.common.model.consumer.view.CartView;
import com.ak.store.common.model.order.dto.OrderDTO;
import com.ak.store.common.model.order.view.OrderView;
import com.ak.store.order.feign.CatalogueFeign;
import com.ak.store.order.model.Order;
import com.ak.store.order.model.OrderProduct;
import com.ak.store.order.service.OrderService;
import com.ak.store.order.util.OrderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final CatalogueFeign catalogueFeign;
    private final OrderMapper orderMapper;

    public List<OrderView> findAllByConsumerId(Long consumerId) {
        var orderList = orderService.findAllByConsumerId(consumerId);
        List<OrderView> orderViewList = orderList.stream()
                .map(orderMapper::mapToOrderView)
                .toList();

        List<Long> productIds = orderList.stream()
                .map(Order::getProducts)
                .flatMap(List::stream)
                .map(OrderProduct::getProductId)
                .toList();

        Map<Long, ProductPoorView> productViewMap = catalogueFeign.findAllProductPoor(productIds).stream()
                .collect(Collectors.toMap(ProductPoorView::getId, product -> product));

        for(var orderView : orderViewList) {
            for(var productView : orderView.getProducts()) {
                productView.setProductPoorView(productViewMap.get(productView.getProductPoorView().getId()));
            }
        }

        return orderViewList;
    }

    @Transactional
    public void createOne(Long consumerId, OrderDTO orderDTO) {
        orderService.createOne(consumerId, orderDTO);
    }
}
