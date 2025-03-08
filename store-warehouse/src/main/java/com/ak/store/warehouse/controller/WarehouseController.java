package com.ak.store.warehouse.controller;

import com.ak.store.common.model.order.dto.OrderProductDTO;
import com.ak.store.common.model.warehouse.dto.ProductCheckDTO;
import com.ak.store.warehouse.facade.WarehouseFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/warehouse/warehouse")
public class WarehouseController {
    private final WarehouseFacade warehouseFacade;

    @PostMapping("check")
    public Boolean checkProductAmount(@RequestBody List<ProductCheckDTO> productCheckDTOList) {
        return warehouseFacade.checkProductAmount(productCheckDTOList);
    }

    @PatchMapping("reserve")
    public void reserveAll(@RequestBody List<OrderProductDTO> orderProductList) {
        warehouseFacade.reserveAllProduct(orderProductList);
    }

    @GetMapping("amount/{productId}")
    public Integer getAmount(@PathVariable Long productId) {
        return warehouseFacade.getAmount(productId);
    }
}
