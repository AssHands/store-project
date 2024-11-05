package com.ak.store.product.controller;

import com.ak.store.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/products/delete")
public class ProductDeleteController {

    private final ProductService productService;

    @Autowired
    public ProductDeleteController(ProductService productService) {
        this.productService = productService;
    }

    @DeleteMapping("{id}")
    public String deleteOneById(@PathVariable("id") Long id) {
        if(productService.deleteOneById(id)) {
            return "DELETED";
        } else {
            return "NOT DELETED";
        }
    }
}