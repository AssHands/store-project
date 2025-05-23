//package com.ak.store.user.feign;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.*;
//
//@FeignClient(name = "warehouse", url = "http://localhost:8083/api/v1/warehouse", configuration = OAuthFeignConfig.class)
//public interface WarehouseFeign {
//
//    @GetMapping("warehouses/{productId}/amount")
//    Integer getAmount(@PathVariable Long productId);
//}
