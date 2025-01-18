package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.service.CatalogueService;
import com.ak.store.common.payload.search.ProductSearchResponse;
import com.ak.store.common.payload.search.SearchAvailableFiltersResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.common.payload.search.SearchAvailableFiltersRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/catalogue/products/search")
public class ProductSearchController {
    private final CatalogueService catalogueService;

    @Autowired
    public ProductSearchController(CatalogueService catalogueService) {
        this.catalogueService = catalogueService;
    }

    @GetMapping
    public ProductSearchResponse searchAllProduct(@RequestBody @Valid ProductSearchRequest productSearchRequest) {
        System.out.println(productSearchRequest);
        return catalogueService.findAllProductBySearch(productSearchRequest);
    }

    @GetMapping("filters")
    public SearchAvailableFiltersResponse searchAllAvailableFilter(@RequestBody @Valid SearchAvailableFiltersRequest searchAvailableFiltersRequest) {
        System.out.println(searchAvailableFiltersRequest);
        return catalogueService.findAvailableFilters(searchAvailableFiltersRequest);
    }
}