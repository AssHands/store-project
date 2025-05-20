package com.ak.store.order.feign;

import com.ak.store.order.model.form.werehouse.AvailableInventoryForm;
import com.ak.store.order.model.form.werehouse.ReserveInventoryForm;
import com.ak.store.order.model.view.werehouse.InventoryView;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "warehouse", url = "http://localhost:8083/api/v1/warehouse", configuration = OAuthFeignConfig.class)
public interface WarehouseFeign {
    @PostMapping("inventory")
    List<InventoryView> findAll(@RequestBody List<Long> productIds);

    @PostMapping("inventory/reserve")
    void reserveAll(@RequestBody @Valid List<ReserveInventoryForm> request);

    @PostMapping("inventory/available")
    Boolean isAvailableAll(@RequestBody @Valid List<AvailableInventoryForm> request);
}