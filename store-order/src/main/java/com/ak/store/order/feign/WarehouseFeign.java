package com.ak.store.order.feign;

import com.ak.store.common.model.order.dto.ProductAmount;
import com.ak.store.common.model.warehouse.dto.ProductCheckDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "warehouse", url = "http://localhost:8083/api/v1/warehouse", configuration = OAuthFeignConfig.class)
public interface WarehouseFeign {

    @PostMapping("warehouses/check")
    Boolean checkProductAmount(@RequestBody List<ProductCheckDTO> productCheckDTOList);

    @PatchMapping("warehouses/reserve")
    void reserveAll(@RequestBody List<ProductAmount> reserveProductDTOList);
}
