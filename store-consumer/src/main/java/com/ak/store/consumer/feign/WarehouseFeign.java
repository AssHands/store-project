package com.ak.store.consumer.feign;

import com.ak.store.common.model.warehouse.dto.ProductCheckDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "warehouse", url = "http://localhost:8083/api/v1/warehouse")
public interface WarehouseFeign {

    @PostMapping("warehouse/check")
    Boolean checkProductAmount(@RequestBody List<ProductCheckDTO> productCheckDTOList);

    @GetMapping("warehouse/amount/{productId}")
    Integer getAmount(@PathVariable Long productId);
}
