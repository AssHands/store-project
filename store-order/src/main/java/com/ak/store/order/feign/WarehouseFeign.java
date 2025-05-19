package com.ak.store.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "warehouse", url = "http://localhost:8083/api/v1/warehouse", configuration = OAuthFeignConfig.class)
public interface WarehouseFeign {

    @PostMapping("warehouses/check")
    Boolean checkProductAmount(@RequestBody Map<Long, Integer> products);

    @PatchMapping("warehouses/reserve")
    void reserveAll(@RequestBody Map<Long, Integer> products);
}
