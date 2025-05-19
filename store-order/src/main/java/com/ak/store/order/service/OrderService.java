package com.ak.store.order.service;

import com.ak.store.order.feign.CatalogueFeign;
import com.ak.store.order.model.dto.OrderDTOPayload;
import com.ak.store.order.model.dto.write.OrderWriteDTO;
import com.ak.store.order.model.entity.Order;
import com.ak.store.order.model.entity.OrderProduct;
import com.ak.store.order.model.view.catalogue.ProductView;
import com.ak.store.order.repository.OrderRepo;
import com.ak.store.order.mapper.OrderMapper;
import com.ak.store.order.validator.service.OrderServiceValidator;
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
    private final OrderServiceValidator orderServiceValidator;
    private final CatalogueFeign catalogueFeign;
    private final OrderMapper orderMapper;

    public List<OrderDTOPayload> findAllByUserId(UUID userId) {
        return orderMapper.toOrderDTOPayload(orderRepo.findAllByConsumerId(userId));
    }

    public OrderDTOPayload createOne(UUID userId, OrderWriteDTO request) {
        orderServiceValidator.validateCreating(request);

        var productIds = new ArrayList<>(request.getProductAmount().keySet());
        var productPriceMap = catalogueFeign.findAllProduct(productIds).stream()
                .collect(Collectors.toMap(ProductView::getId, ProductView::getCurrentPrice));

        var order = Order.builder()
                .userId(userId)
                .build();

        int totalPrice = 0;
        List<OrderProduct> orderProductList = new ArrayList<>();
        for (var product : request.getProductAmount().entrySet()) {
            Long productId = product.getKey();
            Integer productAmount = product.getValue();

            totalPrice += productPriceMap.get(productId) * productAmount;

            orderProductList.add(OrderProduct.builder()
                    .productId(productId)
                    .amount(productAmount)
                    .pricePerOne(productPriceMap.get(productId))
                    .order(order)
                    .build());
        }

        order.setTotalPrice(totalPrice);
        order.setProducts(orderProductList);

        return orderMapper.toOrderDTOPayload(orderRepo.save(order));
    }
}
