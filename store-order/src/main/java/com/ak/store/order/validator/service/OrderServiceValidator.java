package com.ak.store.order.validator.service;


import com.ak.store.order.model.form.werehouse.AvailableInventoryForm;
import com.ak.store.order.repository.ProductRepo;
import com.ak.store.order.repository.InventoryRepo;
import com.ak.store.order.repository.UserBalanceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class OrderServiceValidator {
    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;
    private final UserBalanceRepo userBalanceRepo;

    public void validateCreating(Map<Long, Integer> productMap, UUID userId, Integer totalPrice) {
        List<Long> productIds = new ArrayList<>(productMap.keySet());
        if (!productRepo.isAvailableAllProduct(productIds)) {
            throw new RuntimeException("some of the product are not available");
        }

        List<AvailableInventoryForm> availableForm = new ArrayList<>();
        for (var entry : productMap.entrySet()) {
            availableForm.add(AvailableInventoryForm.builder()
                    .productId(entry.getKey())
                    .amount(entry.getValue())
                    .build());
        }

        if (!inventoryRepo.isAvailableAll(availableForm)) {
            throw new RuntimeException("some of the product are not exist on warehouse");
        }

        if(userBalanceRepo.findOneByUserId(userId).getBalance() < totalPrice) {
            throw new RuntimeException("not enough money");
        }
    }
}
