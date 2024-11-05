package com.ak.store.product.controller;

import com.ak.store.common.dto.ProductFullDTO;
import com.ak.store.common.payload.ProductPayload;
import com.ak.store.product.jdbc.ProductDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/products/create")
public class ProductCreateController {
    @GetMapping
    public ProductDao createOne(@RequestBody ProductPayload createdProduct) {
        return null;
    }
}
