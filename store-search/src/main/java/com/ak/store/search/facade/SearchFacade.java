package com.ak.store.search.facade;

import com.ak.store.common.payload.search.FilterSearchRequest;
import com.ak.store.common.payload.search.FilterSearchResponse;
import com.ak.store.common.payload.search.ProductSearchResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.search.service.ElasticService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchFacade {
    private final ElasticService elasticService;

    public ProductSearchResponse searchAllProduct(ProductSearchRequest productSearchRequest) {
        return elasticService.searchAllProduct(productSearchRequest);
    }

    public FilterSearchResponse searchAllFilter(Jwt accessToken, FilterSearchRequest filterSearchRequest) {
        var response = elasticService.searchAllFilter(filterSearchRequest);

        return response;
    }
}
