package com.ak.store.search.facade;

import com.ak.store.search.kafka.KafkaProducer;
import com.ak.store.search.model.command.SearchFilterCommand;
import com.ak.store.search.model.command.SearchProductCommand;
import com.ak.store.search.model.dto.response.SearchFilterDTO;
import com.ak.store.search.model.dto.response.SearchProductDTO;
import com.ak.store.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchFacade {
    private final SearchService searchService;
    private final KafkaProducer kafkaProducer;

    public SearchProductDTO searchAllProduct(SearchProductCommand command) {
        return searchService.searchAllProduct(command);
    }

    public SearchFilterDTO searchAllFilter(SearchFilterCommand command) {
        var response = searchService.searchAllFilter(command);

        if (command.getUserId() == null) {
            return response;
        }

        kafkaProducer.produceSearchEvent(command.getUserId(), response.getCategoryId());

        return response;
    }
}