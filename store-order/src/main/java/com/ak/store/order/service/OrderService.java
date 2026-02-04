package com.ak.store.order.service;

import com.ak.store.order.mapper.OrderMapper;
import com.ak.store.order.model.command.WriteOrderCommand;
import com.ak.store.order.model.dto.OrderPayloadDTO;
import com.ak.store.order.model.entity.Order;
import com.ak.store.order.model.entity.OrderProduct;
import com.ak.store.order.model.entity.OrderStatus;
import com.ak.store.order.repository.OrderRepo;
import com.ak.store.order.validator.OrderValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final OrderValidator orderValidator;
    private final ProductService productService;
    private final OrderMapper orderMapper;

    public List<OrderPayloadDTO> findAllByUserId(UUID userId) {
        return orderRepo.findAllFullByUserId(userId).stream()
                .map(orderMapper::toPayloadDTO)
                .toList();
    }

    //todo создать фабрику объектов или загуглить что делать при перегрузках метода созданиями кучей объектов
    @Transactional
    public OrderPayloadDTO createOne(WriteOrderCommand command) {
        var productPriceMap = productService.getProductPriceMap(new ArrayList<>(command.getProductAmount().keySet()));

        int totalPrice = getTotalPrice(command, productPriceMap);
        orderValidator.validateCreate(command, totalPrice);

        var order = Order.builder()
                .userId(command.getUserId())
                .time(LocalDateTime.now())
                .status(OrderStatus.IN_PROGRESS)
                .totalPrice(totalPrice)
                .build();

        List<OrderProduct> orderProductList = new ArrayList<>();
        for (var entry : command.getProductAmount().entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                orderProductList.add(OrderProduct.builder()
                        .productId(entry.getKey())
                        .price(productPriceMap.get(entry.getKey()))
                        .order(order)
                        .build());
            }
        }

        order.setProducts(orderProductList);

        return orderMapper.toPayloadDTO(orderRepo.save(order));
    }

    private int getTotalPrice(WriteOrderCommand command, Map<Long, Integer> productPriceMap) {
        int totalPrice = 0;

        for (var entry : command.getProductAmount().entrySet()) {
            totalPrice += productPriceMap.get(entry.getKey()) *  entry.getValue();
        }

        return totalPrice;
    }
}