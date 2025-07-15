package com.ak.store.order.repository;

import com.ak.store.order.feign.WarehouseFeign;
import com.ak.store.order.model.form.werehouse.AvailableInventoryForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class RestInventoryRepo implements InventoryRepo {
    private final WarehouseFeign warehouseFeign;

    @Override
    public boolean isAvailableAll(List<AvailableInventoryForm> request) {
        return warehouseFeign.isAvailableAllInventory(request);
    }
}