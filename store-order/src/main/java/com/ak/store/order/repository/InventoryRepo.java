package com.ak.store.order.repository;

import com.ak.store.order.model.form.werehouse.AvailableInventoryForm;

import java.util.List;

public interface InventoryRepo {
    boolean isAvailableAll(List<AvailableInventoryForm> request);
}