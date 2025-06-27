package com.ak.store.order.validator.service;


import com.ak.store.order.feign.CatalogueFeign;
import com.ak.store.order.feign.WarehouseFeign;
import com.ak.store.order.model.dto.write.OrderWriteDTO;
import com.ak.store.order.model.form.werehouse.AvailableInventoryForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class OrderServiceValidator {
    private final CatalogueFeign catalogueFeign;
    private final WarehouseFeign warehouseFeign;

    public void validateCreating(Map<Long, Integer> productMap, Integer totalPrice) {
        List<Long> productIds = new ArrayList<>(productMap.keySet());
        if (!catalogueFeign.isAvailableAllProduct(productIds)) {
            throw new RuntimeException("some of the product are not available");
        }

        List<AvailableInventoryForm> availableForm = new ArrayList<>();
        for (var entry : productMap.entrySet()) {
            availableForm.add(AvailableInventoryForm.builder()
                    .productId(entry.getKey())
                    .amount(entry.getValue())
                    .build());
        }

        if (!warehouseFeign.isAvailableAll(availableForm)) {
            throw new RuntimeException("some of the product are not exist on warehouse");
        }

        //todo add payment validation
    }
}
