package com.ak.store.warehouseSagaWorker.service;

import com.ak.store.warehouseSagaWorker.model.entity.Inventory;
import com.ak.store.warehouseSagaWorker.repository.InventoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class InventoryService {
    private final InventoryRepo inventoryRepo;

    public void releaseAll(Map<Long, Integer> productAmount) {
        var inventories = inventoryRepo.findAllById(productAmount.keySet());

        for (var inventory : inventories) {
            inventory.setAmount(inventory.getAmount() + productAmount.get(inventory.getProductId()));
        }

        inventoryRepo.saveAll(inventories);
    }

    public void reserveAll(Map<Long, Integer> productAmount) {
        var inventories = inventoryRepo.findAllById(productAmount.keySet());

        for (var inventory : inventories) {
            int amount = inventory.getAmount() - productAmount.get(inventory.getProductId());

            if (amount < 0) {
                throw new RuntimeException("not enough amount");
            }
        }

        for (var inventory : inventories) {
            inventory.setAmount(inventory.getAmount() - productAmount.get(inventory.getProductId()));
        }

        inventoryRepo.saveAll(inventories);
    }

    public void createOne(Long productId) {
        var inventory = Inventory.builder()
                .productId(productId)
                .amount(0)
                .build();

        inventoryRepo.save(inventory);
    }

    public void deleteOne(Long productId) {
        inventoryRepo.deleteById(productId);
    }
}