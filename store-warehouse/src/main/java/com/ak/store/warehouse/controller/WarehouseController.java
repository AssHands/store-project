package com.ak.store.warehouse.controller;

import com.ak.store.common.model.order.dto.ProductAmountDTO;
import com.ak.store.common.model.warehouse.dto.ProductCheckDTO;
import com.ak.store.warehouse.facade.WarehouseFacade;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/warehouse/warehouses")
public class WarehouseController {
    private final WarehouseFacade warehouseFacade;

    @PostMapping("check")
    public Boolean checkProductAmount(@RequestBody List<ProductAmountDTO> productCheckDTOList) {
        return warehouseFacade.checkProductAmount(productCheckDTOList);
    }

    @PatchMapping("reserve")
    public void reserveAll(@RequestBody List<ProductAmountDTO> orderProductList) {
        warehouseFacade.reserveAllProduct(orderProductList);
    }

    @GetMapping("{productId}/amount")
    public Integer getAmount(@PathVariable Long productId) {
        return warehouseFacade.getAmount(productId);
    }

    @PatchMapping("{productId}/amount/add")
    public Long addAmount(@PathVariable Long productId, @RequestBody @Positive Integer addAmount) {
        return warehouseFacade.addAmount(productId, addAmount);
    }

    @PatchMapping("{productId}/amount/delete")
    public Long deleteAmount(@PathVariable Long productId, @RequestBody @Positive Integer deleteAmount) {
        return warehouseFacade.deleteAmount(productId, deleteAmount);
    }
}
