package com.ak.store.search.controller;

import com.ak.store.common.payload.search.ProductSearchResponse;
import com.ak.store.common.payload.search.FilterSearchRequest;
import com.ak.store.common.payload.search.FilterSearchResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.search.facade.SearchFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/search")
public class SearchController {
    private final SearchFacade searchFacade;

    @PostMapping("products")
    public ProductSearchResponse searchAllProduct(@RequestBody @Valid ProductSearchRequest productSearchRequest) {
        return searchFacade.searchAllProduct(productSearchRequest);
    }

    @PostMapping("filters")
    public FilterSearchResponse searchAllAvailableFilters(@AuthenticationPrincipal Jwt accessToken,
                                                          @RequestBody @Valid FilterSearchRequest filterSearchRequest) {
        return searchFacade.searchAllFilter(accessToken, filterSearchRequest);
    }
}
