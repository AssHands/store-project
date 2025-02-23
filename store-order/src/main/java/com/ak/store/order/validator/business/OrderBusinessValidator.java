package com.ak.store.order.validator.business;


import com.ak.store.common.model.order.dto.OrderDTO;
import com.ak.store.common.model.order.dto.OrderProductDTO;
import com.ak.store.common.model.warehouse.dto.ProductCheckDTO;
import com.ak.store.order.feign.CatalogueFeign;
import com.ak.store.order.feign.WarehouseFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderBusinessValidator {
    private final CatalogueFeign catalogueFeign;
    private final WarehouseFeign warehouseFeign;

    public void validateCreation(OrderDTO orderDTO) {
        var productIds = orderDTO.getProducts().stream()
                .map(OrderProductDTO::getProductId)
                .toList();

        if (!catalogueFeign.availableAll(productIds)) {
            throw new RuntimeException("some of the product are not available");
        }

        List<ProductCheckDTO> productCheckDTOList = new ArrayList<>();
        for (var orderProduct : orderDTO.getProducts()) {
            productCheckDTOList.add(ProductCheckDTO.builder()
                    .productId(orderProduct.getProductId())
                    .amount(orderProduct.getAmount())
                    .build());
        }

        if(!warehouseFeign.checkProductAmount(productCheckDTOList)) {
            throw new RuntimeException("some of the product are not exist on warehouse");
        }
    }
}
