package com.ak.store.warehouse.serivce;

import com.ak.store.warehouse.mapper.InventoryMapper;
import com.ak.store.warehouse.model.dto.AvailableInventoryDTO;
import com.ak.store.warehouse.model.dto.InventoryDTO;
import com.ak.store.warehouse.model.dto.ReserveInventoryDTO;
import com.ak.store.warehouse.model.entity.Inventory;
import com.ak.store.warehouse.repostitory.InventoryRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class InventoryService {
    private final InventoryRepo inventoryRepo;
    private final InventoryMapper inventoryMapper;

    private List<Inventory> findAllWithWriteLockByProductIds(List<Long> productIds) {
        return inventoryRepo.findAllLockedPessimisticReadByProductIdIn(productIds);
    }

    private Inventory findOneWithWriteLockByProductId(Long productId) {
        return inventoryRepo.findOneLockedPessimisticReadByProductId(productId)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public List<InventoryDTO> findAllByProductIds(List<Long> productIds) {
        return inventoryMapper.toInventoryDTO(inventoryRepo.findAllByProductIdIn(productIds));
    }

    @Transactional
    public void reserveAll(List<ReserveInventoryDTO> request) {
        var reserveMap = request.stream().collect(Collectors.toMap(
                ReserveInventoryDTO::getProductId, ReserveInventoryDTO::getAmount));

        List<Inventory> inventories = findAllWithWriteLockByProductIds(new ArrayList<>(reserveMap.keySet()));

        for (var inventory : inventories) {
            Long productId = inventory.getProductId();
            Integer amount = inventory.getAmount();

            amount -= reserveMap.get(productId);

            //todo мб перенести в валидатор?
            if (amount < 0) {
                throw new RuntimeException("not enough amount, product id=%d".formatted(productId));
            }

            inventory.setAmount(amount);
        }

        inventoryRepo.saveAll(inventories);
    }

    @Transactional
    public InventoryDTO createOne(Long productId) {
        var inventory = inventoryRepo.save(Inventory.builder()
                .productId(productId)
                .build());

        return inventoryMapper.toInventoryDTO(inventory);
    }

    @Transactional
    public InventoryDTO increaseAmount(Long productId, Integer amount) {
        var inventory = findOneWithWriteLockByProductId(productId);

        int newAmount = inventory.getAmount();
        newAmount += amount;
        inventory.setAmount(newAmount);

        return inventoryMapper.toInventoryDTO(inventoryRepo.save(inventory));
    }

    @Transactional
    public InventoryDTO reduceAmount(Long productId, Integer amount) {
        var inventory = findOneWithWriteLockByProductId(productId);

        int newAmount = inventory.getAmount();
        newAmount -= amount;

        //todo мб перенести в валидатор?
        if (newAmount < 0) {
            throw new RuntimeException("amount below zero");
        }

        inventory.setAmount(newAmount);
        return inventoryMapper.toInventoryDTO(inventoryRepo.save(inventory));
    }

    public Boolean isAvailableAll(List<AvailableInventoryDTO> request) {
        List<Long> ids = request.stream()
                .map(AvailableInventoryDTO::getProductId)
                .toList();

        var inventoryMap = inventoryRepo.findAllByProductIdIn(ids).stream()
                .collect(Collectors.toMap(Inventory::getProductId, Inventory::getAmount));

        for (var inventoryRequest : request) {
            Long productId = inventoryRequest.getProductId();
            Integer amount = inventoryRequest.getAmount();
            Integer requestAmount = inventoryMap.get(productId);

            if (requestAmount == null || amount < requestAmount) {
                return false;
            }
        }

        return true;
    }
}