package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.service.ProductService;
import com.ak.store.common.payload.search.ProductSearchResponse;
import com.ak.store.common.payload.search.AvailableFiltersResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.common.payload.search.SearchAvailableFilters;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/catalogue/products/search")
public class ProductSearchController {
    private final ProductService productService;

    @Autowired
    public ProductSearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ProductSearchResponse searchAllProduct(@RequestBody @Valid ProductSearchRequest productSearchRequest) {
        System.out.println(productSearchRequest);
        return productService.findAllBySearch(productSearchRequest);
    }

    @GetMapping("filters")
    public AvailableFiltersResponse searchAllAvailableFilter(@RequestBody @Valid SearchAvailableFilters searchAvailableFilters) {
        System.out.println(searchAvailableFilters);
        return productService.facet(searchAvailableFilters);
    }
}