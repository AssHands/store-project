package com.ak.store.product.controller;

import com.ak.store.common.dto.ProductDTO;
import com.ak.store.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/products/update")
public class ProductUpdateController {

    private final ProductService productService;

    @Autowired
    public ProductUpdateController(ProductService productService) {
        this.productService = productService;
    }

    @PatchMapping("{id}")
    public ProductDTO updateOneById(@PathVariable("id") Long id,
                                    @RequestBody Map<String, ? super Object> updatedFields) {
        productService.updateOneById(id, updatedFields);

        return null;
    }
}
