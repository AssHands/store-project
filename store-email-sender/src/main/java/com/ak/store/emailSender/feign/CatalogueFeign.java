package com.ak.store.emailSender.feign;

import com.ak.store.emailSender.model.view.ProductView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "catalogue", url = "http://localhost:8080/api/v1/catalogue")
public interface CatalogueFeign {
    @PostMapping("products/find")
    List<ProductView> findAllProduct(@RequestBody List<Long> ids);
}