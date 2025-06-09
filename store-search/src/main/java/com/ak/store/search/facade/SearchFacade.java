package com.ak.store.search.facade;

import com.ak.store.common.event.search.SearchAllEvent;
import com.ak.store.search.kafka.EventProducerKafka;
import com.ak.store.search.model.dto.request.FilterSearchRequestDTO;
import com.ak.store.search.model.dto.request.ProductSearchRequestDTO;
import com.ak.store.search.model.dto.response.FilterSearchResponseDTO;
import com.ak.store.search.model.dto.response.ProductSearchResponseDTO;
import com.ak.store.search.service.SearchElasticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SearchFacade {
    private final SearchElasticService searchElasticService;
    private final EventProducerKafka eventProducerKafka;

    public ProductSearchResponseDTO searchAllProduct(ProductSearchRequestDTO request) {
        return searchElasticService.searchAllProduct(request);
    }

    public FilterSearchResponseDTO searchAllFilter(UUID userId, FilterSearchRequestDTO request) {
        var response = searchElasticService.searchAllFilter(request);

        if (userId == null) {
            return response;
        }

        var event = SearchAllEvent.builder()
                .userId(userId)
                .categoryId(response.getCategoryId())
                .build();

        //todo: make async
        eventProducerKafka.send(event);

        return response;
    }
}