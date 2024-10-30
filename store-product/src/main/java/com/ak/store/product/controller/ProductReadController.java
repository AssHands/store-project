package com.ak.store.product.controller;

import com.ak.store.common.ResponseObject.ProductPageResponse;
import com.ak.store.common.ResponseObject.ProductResponse;
import com.ak.store.product.jdbc.ProductDao;
import com.ak.store.product.service.ProductService;
import com.ak.store.queryGenerator.QueryGenerator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/products/get")
public class ProductReadController {

    private final ProductService productService;
    private final ProductDao productDao;

    @Autowired
    public ProductReadController(ProductService userService, ProductDao userDao) {
        this.productService = userService;
        this.productDao = userDao;
    }


    @GetMapping()
    public List<ProductResponse> getAll(@RequestParam(defaultValue = "popular") String sort,
                                       @RequestParam(defaultValue = "0") @Min(0) int offset,
                                       @RequestParam(defaultValue = "18") @Min(1) @Max(100) int limit,
                                       @RequestBody Map<String, String> filters) {

        List<String> allowsSortParams = List.of("popular", "priceup", "pricedown", "sale", "rate", "newly");
        if(!allowsSortParams.contains(sort)) {
            throw new RuntimeException("Incorrect sort param");
        }

        return productDao.findAll(sort, offset, limit, filters);
    }
}