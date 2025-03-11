package com.ak.store.consumer.feign;

import com.ak.store.common.model.warehouse.dto.ProductCheckDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "warehouse", url = "http://localhost:8083/api/v1/warehouse", configuration = OAuthFeignConfig.class)
public interface WarehouseFeign {

    @GetMapping("warehouses/{productId}/amount")
    Integer getAmount(@PathVariable Long productId);
}
