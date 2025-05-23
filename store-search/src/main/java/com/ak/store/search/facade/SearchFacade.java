package com.ak.store.search.facade;

import com.ak.store.search.kafka.SearchProducerKafka;
import com.ak.store.search.model.dto.request.FilterSearchRequestDTO;
import com.ak.store.search.model.dto.request.ProductSearchRequestDTO;
import com.ak.store.search.model.dto.response.FilterSearchResponseDTO;
import com.ak.store.search.model.dto.response.ProductSearchResponseDTO;
import com.ak.store.search.service.SearchElasticService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchFacade {
    private final SearchElasticService searchElasticService;
    private final SearchProducerKafka searchProducerKafka;

    public ProductSearchResponseDTO searchAllProduct(ProductSearchRequestDTO request) {
        return searchElasticService.searchAllProduct(request);
    }

    public FilterSearchResponseDTO searchAllFilter(Jwt accessToken, FilterSearchRequestDTO request) {
        var response = searchElasticService.searchAllFilter(request);

        if (accessToken == null) {
            return response;
        }

        //todo: make async
//        searchProducerKafka.send(SearchAllEvent.builder()
//                .consumerSearch(ConsumerSearchDTO.builder()
//                        .consumerId(accessToken.getSubject())
//                        .categoryId(response.getCategoryId())
//                        .build())
//                .build());

        return response;
    }
}