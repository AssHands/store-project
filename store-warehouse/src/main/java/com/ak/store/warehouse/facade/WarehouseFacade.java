package com.ak.store.warehouse.facade;

import com.ak.store.common.model.warehouse.dto.ProductCheckDTO;
import com.ak.store.common.model.warehouse.dto.ReserveProductDTO;
import com.ak.store.warehouse.serivce.WarehouseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WarehouseFacade {

    private final WarehouseService warehouseService;
    public Boolean checkProductAmount(List<ProductCheckDTO> productCheckDTOList) {
        return warehouseService.checkProductAmount(productCheckDTOList);
    }

    @Transactional
    public void reserveAllProduct(List<ReserveProductDTO> reserveProductDTOList) {
        warehouseService.reserveAllProduct(reserveProductDTOList);
    }

    public Integer getAmount(Long productId) {
        return warehouseService.getAmount(productId);
    }
}
