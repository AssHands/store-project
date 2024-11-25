package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.service.ProductService;
import com.ak.store.common.dto.search.FacetFilter;
import com.ak.store.common.payload.product.ProductSearchResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/products")
public class ProductSearchController {
    private final ProductService productService;

    @Autowired
    public ProductSearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("search")
    public ProductSearchResponse search(@RequestBody @Valid ProductSearchRequest productSearchRequest) {
        System.out.println(productSearchRequest);
        return productService.findAllBySearch(productSearchRequest);
    }

    @GetMapping("facet")
    public FacetFilter facet() {
        return productService.facet();
    }
}