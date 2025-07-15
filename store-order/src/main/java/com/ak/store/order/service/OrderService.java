package com.ak.store.order.service;

import com.ak.store.order.mapper.OrderMapper;
import com.ak.store.order.model.dto.OrderDTOPayload;
import com.ak.store.order.model.entity.Order;
import com.ak.store.order.model.entity.OrderProduct;
import com.ak.store.order.model.entity.OrderStatus;
import com.ak.store.order.model.view.feign.ProductView;
import com.ak.store.order.repository.OrderRepo;
import com.ak.store.order.repository.ProductRepo;
import com.ak.store.order.validator.service.OrderServiceValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final OrderServiceValidator orderServiceValidator;
    private final OrderMapper orderMapper;
    private final ProductRepo productRepo;

    public List<OrderDTOPayload> findAllByUserId(UUID userId) {
        return orderMapper.toOrderDTOPayload(orderRepo.findAllWithProductsByUserId(userId));
    }

    @Transactional
    public OrderDTOPayload createOne(UUID userId, Map<Long, Integer> productMap) {
        var productPriceMap = productRepo.findAllByIds(new ArrayList<>(productMap.keySet())).stream()
                .collect(Collectors.toMap(ProductView::getId, ProductView::getCurrentPrice));

        var order = Order.builder()
                .userId(userId)
                .time(LocalDateTime.now())
                .status(OrderStatus.IN_PROGRESS)
                .build();

        int totalPrice = 0;
        List<OrderProduct> orderProductList = new ArrayList<>();
        for (var entry : productMap.entrySet()) {
            Long productId = entry.getKey();
            Integer productAmount = entry.getValue();

            totalPrice += productPriceMap.get(productId) * productAmount;

            for (int i = 0; i < productAmount; i++) {
                orderProductList.add(OrderProduct.builder()
                        .productId(productId)
                        .price(productPriceMap.get(productId))
                        .order(order)
                        .build());
            }
        }

        orderServiceValidator.validateCreating(productMap, userId, totalPrice);

        order.setTotalPrice(totalPrice);
        order.setProducts(orderProductList);

        return orderMapper.toOrderDTOPayload(orderRepo.save(order));
    }
}