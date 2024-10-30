package com.ak.store.product.controller;

import com.ak.store.common.ResponseObject.ProductResponse;
import com.ak.store.product.jdbc.ProductDao;
import com.ak.store.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductReadController {

    private final ProductService productService;
    private final ProductDao productDao;

    @Autowired
    public ProductReadController(ProductService userService, ProductDao userDao) {
        this.productService = userService;
        this.productDao = userDao;
    }


    @GetMapping("pp")
    public List<ProductResponse> a(@RequestBody HashMap<String, String> filters) {
        return productDao.a(filters);
    }
}