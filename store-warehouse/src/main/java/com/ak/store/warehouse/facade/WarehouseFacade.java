package com.ak.store.warehouse.facade;

import com.ak.store.warehouse.serivce.WarehouseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WarehouseFacade {

    private final WarehouseService warehouseService;
    public Boolean checkProductAmount(List<ProductAmount> productCheckDTOList) {
        return warehouseService.checkProductAmount(productCheckDTOList);
    }

    @Transactional
    public void reserveAllProduct(List<ProductAmount> productCheckDTOList) {
        warehouseService.reserveAllProduct(productCheckDTOList);
    }

    @Transactional
    public Long createProductWarehouse(Long productId) {
        return warehouseService.createProductWarehouse(productId).getId();
    }

    public Integer getAmount(Long productId) {
        return warehouseService.getAmount(productId);
    }

    @Transactional
    public Long addAmount(Long productId, int addAmount) {
        return warehouseService.addAmount(productId, addAmount).getId();
    }

    @Transactional
    public Long deleteAmount(Long productId, Integer deleteAmount) {
        return warehouseService.deleteAmount(productId, deleteAmount).getId();
    }
}
