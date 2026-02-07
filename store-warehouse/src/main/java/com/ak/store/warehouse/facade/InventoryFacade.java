package com.ak.store.warehouse.facade;

import com.ak.store.warehouse.model.command.ReserveInventoryCommand;
import com.ak.store.warehouse.model.command.AvailableInventoryCommand;
import com.ak.store.warehouse.model.dto.InventoryDTO;
import com.ak.store.warehouse.serivce.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InventoryFacade {
    private final InventoryService inventoryService;

    public List<InventoryDTO> findAll(List<Long> productIds) {
        return inventoryService.findAllByProductIds(productIds);
    }

    @Transactional
    public void reserveAll(List<ReserveInventoryCommand> commands) {
        inventoryService.reserveAll(commands);
    }

    @Transactional
    public Long createOne(Long productId) {
        return inventoryService.createOne(productId).getProductId();
    }

    @Transactional
    public Long increaseAmount(Long productId, Integer amount) {
        return inventoryService.increaseAmount(productId, amount).getProductId();
    }

    @Transactional
    public Long reduceAmount(Long productId, Integer amount) {
        return inventoryService.reduceAmount(productId, amount).getProductId();
    }

    public Boolean isAvailableAll(List<AvailableInventoryCommand> commands) {
        return inventoryService.isAvailableAll(commands);
    }
}
