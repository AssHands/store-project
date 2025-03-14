package com.ak.store.order.service;

import com.ak.store.common.model.catalogue.dto.ProductPriceDTO;
import com.ak.store.common.model.order.form.OrderForm;
import com.ak.store.common.model.order.dto.ProductAmount;
import com.ak.store.order.feign.CatalogueFeign;
import com.ak.store.order.model.Order;
import com.ak.store.order.model.OrderProduct;
import com.ak.store.order.repository.OrderRepo;
import com.ak.store.order.validator.business.OrderBusinessValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final OrderBusinessValidator orderBusinessValidator;
    private final CatalogueFeign catalogueFeign;

    public List<Order> findAllByConsumerId(String consumerId) {
        return orderRepo.findAllWithProductsByConsumerId(UUID.fromString(consumerId));
    }

    public Order createOne(String consumerId, OrderForm orderForm) {
        orderBusinessValidator.validateCreation(orderForm);

        var productIds = orderForm.getProducts().stream()
                .map(ProductAmount::getProductId)
                .toList();

        var productPriceMap = catalogueFeign.getAllPrice(productIds).stream()
                .collect(Collectors.toMap(ProductPriceDTO::getId, ProductPriceDTO::getPrice));

        var order = Order.builder()
                .consumerId(UUID.fromString(consumerId))
                .build();

        int totalPrice = 0;
        List<OrderProduct> orderProductList = new ArrayList<>();
        for (var orderProductDTO : orderForm.getProducts()) {
            totalPrice += orderProductDTO.getAmount() * productPriceMap.get(orderProductDTO.getProductId());

            orderProductList.add(OrderProduct.builder()
                    .productId(orderProductDTO.getProductId())
                    .amount(orderProductDTO.getAmount())
                    .pricePerOne(productPriceMap.get(orderProductDTO.getProductId()))
                    .order(order)
                    .build());
        }

        order.setTotalPrice(totalPrice);
        order.setProducts(orderProductList);

        return orderRepo.save(order);
    }
}
