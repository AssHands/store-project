//package com.ak.store.user.feign;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import java.util.List;
//
//@FeignClient(value = "catalogue", url = "http://localhost:8080/api/v1/catalogue", configuration = OAuthFeignConfig.class)
//public interface CatalogueFeign {
//    @PostMapping("products/poor")
//    List<ProductPoorView> findAllProductPoor(@RequestBody List<Long> ids);
//
//    @GetMapping("products/exist/{id}")
//    Boolean existOne(@PathVariable Long id);
//
//    @GetMapping("products/available/{id}")
//    Boolean availableOne(@PathVariable Long id);
//}
