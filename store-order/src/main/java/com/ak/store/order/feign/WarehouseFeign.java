package com.ak.store.order.feign;

import com.ak.store.order.model.form.werehouse.AvailableInventoryForm;
import com.ak.store.order.model.form.werehouse.ReserveInventoryForm;
import com.ak.store.order.model.view.feign.InventoryView;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "warehouse", url = "http://localhost:8083/api/v1/warehouse", configuration = OAuthFeignConfig.class)
public interface WarehouseFeign {
    @PostMapping("inventory/available")
    Boolean isAvailableAllInventory(@RequestBody @Valid List<AvailableInventoryForm> request);
}