package com.ak.store.warehouseSagaWorker.repository;

import com.ak.store.warehouseSagaWorker.model.inventory.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepo extends JpaRepository<Inventory, Long> {
}
