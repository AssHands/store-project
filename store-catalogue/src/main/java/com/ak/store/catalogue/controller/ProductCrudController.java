package com.ak.store.catalogue.controller;

import com.ak.store.common.dto.product.ProductDTO;
import com.ak.store.catalogue.service.ProductService;
import com.ak.store.catalogue.utils.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/products")
public class ProductCrudController {

    private final ProductService productService;
    private final ProductValidator productValidator;


    @Autowired
    public ProductCrudController(ProductService userService, ProductValidator productValidator) {
        this.productService = userService;
        this.productValidator = productValidator;
    }

    @GetMapping("product/{id}")
    public ProductDTO getOneById(@PathVariable("id") Long id) {
        return productService.findOneById(id);
    }

    @DeleteMapping("product/{id}")
    public String deleteOneById(@PathVariable("id") Long id) {
        if(productService.deleteOneById(id))
            return "DELETED";

        return "NOT DELETED";
    }
}