package com.ak.store.order.validator;

import com.ak.store.order.model.command.WriteOrderCommand;
import com.ak.store.order.model.form.werehouse.AvailableInventoryForm;
import com.ak.store.order.repository.ProductRepo;
import com.ak.store.order.repository.InventoryRepo;
import com.ak.store.order.repository.UserBalanceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderValidator {
    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;
    private final UserBalanceRepo userBalanceRepo;

    public void validateCreate(WriteOrderCommand command, int totalPrice) {
        allProductAvailable(command);
        allProductAvailableInWarehouse(command);
        userHasEnoughMoney(command, totalPrice);
    }

    private void allProductAvailable(WriteOrderCommand command) {
        List<Long> productIds = new ArrayList<>(command.getProductAmount().keySet());

        if (!productRepo.isAvailableAllProduct(productIds)) {
            throw new RuntimeException("some of the products are not available");
        }
    }

    private void allProductAvailableInWarehouse(WriteOrderCommand command) {
        List<AvailableInventoryForm> availableForm = new ArrayList<>();
        for (var entry : command.getProductAmount().entrySet()) {
            availableForm.add(AvailableInventoryForm.builder()
                    .productId(entry.getKey())
                    .amount(entry.getValue())
                    .build());
        }

        if (!inventoryRepo.isAvailableAll(availableForm)) {
            throw new RuntimeException("some of the products are not exist on warehouse");
        }
    }

    private void userHasEnoughMoney(WriteOrderCommand command, int totalPrice) {
        if(userBalanceRepo.findOneByUserId(command.getUserId()).getBalance() < totalPrice) {
            throw new RuntimeException("not enough money");
        }
    }
}
