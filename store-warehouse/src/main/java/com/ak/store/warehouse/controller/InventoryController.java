package com.ak.store.warehouse.controller;

import com.ak.store.warehouse.facade.InventoryFacade;
import com.ak.store.warehouse.mapper.InventoryMapper;
import com.ak.store.warehouse.model.form.AvailableInventoryForm;
import com.ak.store.warehouse.model.form.ReserveInventoryForm;
import com.ak.store.warehouse.model.view.InventoryView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/warehouse/inventory")
public class InventoryController {
    private final InventoryFacade inventoryFacade;
    private final InventoryMapper inventoryMapper;

    @PostMapping
    public List<InventoryView> findAll(@RequestBody List<Long> productIds) {
        return inventoryFacade.findAll(productIds).stream()
                .map(inventoryMapper::toView)
                .toList();
    }

    @PostMapping("available")
    public Boolean isAvailableAll(@RequestBody @Valid List<AvailableInventoryForm> forms) {
        return inventoryFacade.isAvailableAll(
                forms.stream()
                        .map(inventoryMapper::toAvailableInventoryCommand)
                        .toList()
        );
    }

    @PostMapping("reserve")
    public void reserveAll(@RequestBody @Valid List<ReserveInventoryForm> forms) {
        inventoryFacade.reserveAll(
                forms.stream()
                        .map(inventoryMapper::toReserveInventoryCommand)
                        .toList()
        );
    }

    @PatchMapping("{productId}/amount/increase")
    public Long increaseAmount(@PathVariable Long productId, @RequestParam @Positive Integer amount) {
        return inventoryFacade.increaseAmount(productId, amount);
    }

    @PatchMapping("{productId}/amount/reduce")
    public Long reduceAmount(@PathVariable Long productId, @RequestParam @Positive Integer amount) {
        return inventoryFacade.reduceAmount(productId, amount);
    }
}