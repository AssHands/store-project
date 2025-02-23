package com.ak.store.order.feign;

import com.ak.store.common.model.catalogue.view.ProductPoorView;
import com.ak.store.common.model.catalogue.view.ProductPrice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "catalogue", url = "http://localhost:8080/api/v1/catalogue")
public interface CatalogueFeign {
    @PostMapping("products/poor")
    List<ProductPoorView> findAllProductPoor(@RequestBody List<Long> ids);

    @PostMapping("products/available")
    Boolean availableAll(@RequestBody List<Long> ids);

    @PostMapping("products/price")
    List<ProductPrice> getAllPrice(@RequestBody List<Long> ids);
}
