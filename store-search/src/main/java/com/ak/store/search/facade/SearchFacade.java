package com.ak.store.search.facade;

import com.ak.store.common.event.search.SearchAllEvent;
import com.ak.store.common.model.search.dto.ConsumerSearchDTO;
import com.ak.store.common.payload.search.FilterSearchRequest;
import com.ak.store.common.payload.search.FilterSearchResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.common.payload.search.ProductSearchResponse;
import com.ak.store.search.kafka.SearchProducerKafka;
import com.ak.store.search.service.SearchElasticService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchFacade {
    private final SearchElasticService searchElasticService;
    private final SearchProducerKafka searchProducerKafka;

    public ProductSearchResponse searchAllProduct(ProductSearchRequest productSearchRequest) {
        return searchElasticService.searchAllProduct(productSearchRequest);
    }

    public FilterSearchResponse searchAllFilter(Jwt accessToken, FilterSearchRequest filterSearchRequest) {
        var response = searchElasticService.searchAllFilter(filterSearchRequest);

        if(accessToken == null) {
            return response;
        }

        //todo: make async
        searchProducerKafka.send(SearchAllEvent.builder()
                .consumerSearch(ConsumerSearchDTO.builder()
                        .consumerId(accessToken.getSubject())
                        .categoryId(response.getCategoryId())
                        .build())
                .build());

        return response;
    }
}