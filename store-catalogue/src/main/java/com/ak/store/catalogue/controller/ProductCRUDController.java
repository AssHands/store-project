package com.ak.store.catalogue.controller;

import com.ak.store.common.dto.product.ProductDTO;
import com.ak.store.catalogue.service.ProductService;
import com.ak.store.catalogue.utils.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/products")
public class ProductCRUDController {

    private final ProductService productService;
    private final ProductValidator productValidator;


    @Autowired
    public ProductCRUDController(ProductService userService, ProductValidator productValidator) {
        this.productService = userService;
        this.productValidator = productValidator;
    }


    @GetMapping("get/{id}")
    public ProductDTO getOneByIdPreview(@PathVariable("id") Long id) {
        return productService.findOneById(id);
    }
}