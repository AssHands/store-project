package com.ak.store.order.feign;

import com.ak.store.order.model.view.feign.ProductView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "catalogue", url = "http://localhost:8080/api/v1/catalogue")
public interface CatalogueFeign {
    @PostMapping("products/find")
    List<ProductView> findAllProductByIds(@RequestBody List<Long> ids);

    @PostMapping("products/available")
    Boolean isAvailableAllProduct(@RequestBody List<Long> ids);
}
