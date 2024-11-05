package com.ak.store.product.controller;

import com.ak.store.common.dto.ProductDTO;
import com.ak.store.common.payload.ProductPayload;
import com.ak.store.product.service.ProductService;
import com.ak.store.product.utils.ProductValidator;
import com.ak.store.queryGenerator.UpdateQueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.SimpleTimeZone;


@RestController
@RequestMapping("api/v1/products/update")
public class ProductUpdateController {

    private final ProductService productService;
    private final ProductValidator productValidator;


    @Autowired
    public ProductUpdateController(ProductService productService, ProductValidator productValidator) {
        this.productService = productService;
        this.productValidator = productValidator;
    }

    @PatchMapping("{id}")
    public ProductDTO updateOneById(@PathVariable("id") Long id,
                                    @RequestBody ProductPayload updatedProduct) {
        return productService.updateOneById(id, updatedProduct);
    }
}
