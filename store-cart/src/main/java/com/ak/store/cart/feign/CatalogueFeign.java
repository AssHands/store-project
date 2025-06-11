package com.ak.store.cart.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "catalogue", url = "http://localhost:8080/api/v1/catalogue")
public interface CatalogueFeign {
    @PostMapping("products/exist")
    Boolean isExistAllProduct(@RequestBody List<Long> ids);
}