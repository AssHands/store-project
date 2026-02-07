package com.ak.store.search.service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.ak.store.search.mapper.SearchMapper;
import com.ak.store.search.model.command.SearchFilterCommand;
import com.ak.store.search.model.command.SearchProductCommand;
import com.ak.store.search.model.document.Product;
import com.ak.store.search.model.dto.*;
import com.ak.store.search.model.dto.response.SearchFilterDTO;
import com.ak.store.search.model.dto.response.SearchProductDTO;
import com.ak.store.search.repository.SearchRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class SearchService {
    private final SearchMapper searchMapper;
    private final SearchRepo searchRepo;


    public SearchFilterDTO searchAllFilter(SearchFilterCommand command) {
        if (command.getCategoryId() == null) {
            Optional<Long> categoryIdOpt = searchRepo.defineCategory(command.getText());

            if (categoryIdOpt.isEmpty()) {
                return new SearchFilterDTO();
            }

            command.setCategoryId(categoryIdOpt.get());
        }

        var response = searchRepo.searchAllFilter(command);

        return SearchFilterDTO.builder()
                .filters(getFiltersDTO(response))
                .categoryId(command.getCategoryId())
                .build();
    }

    public SearchProductDTO searchAllProduct(SearchProductCommand command) {
        if (command.getCategoryId() == null) {
            Optional<Long> categoryIdOpt = searchRepo.defineCategory(command.getText());

            if (categoryIdOpt.isEmpty()) {
                return new SearchProductDTO();
            }

            command.setCategoryId(categoryIdOpt.get());
        }

        var response = searchRepo.searchAllProduct(command);

        return SearchProductDTO.builder()
                .content(getContent(response))
                .searchAfter(getSearchAfter(response))
                .build();
    }

    //todo: move to utils class?
    private FiltersDTO getFiltersDTO(SearchResponse<Product> response) {
        var filtersDTO = new FiltersDTO();

        for (var allAggs : response.aggregations().entrySet()) {
            Long characteristicId = Long.parseLong(allAggs.getKey());
            Set<Map.Entry<String, Aggregate>> entrySet = null;

            if (allAggs.getValue().isFilter()) {
                entrySet = allAggs.getValue().filter().aggregations().get("1").nested()
                        .aggregations().entrySet().iterator().next().getValue().filter().aggregations().entrySet();
            }

            if (entrySet == null)
                entrySet = allAggs.getValue().nested().aggregations().get("2").filter().aggregations().entrySet();

            for (var entry : entrySet) {
                if (entry.getValue().isRange()) {
                    List<NumericFilterValueDTO> rangeValues = new ArrayList<>();

                    for (var agg : entry.getValue().range().buckets().array()) {
                        if (agg.docCount() == 0) continue;
                        rangeValues.add(NumericFilterValueDTO.builder()
                                .fromValue(agg.from().intValue())
                                .toValue(agg.to().intValue() - 1)
                                .build());
                    }

                    if (rangeValues.isEmpty()) continue;
                    filtersDTO.getNumericFilters().add(NumericFilterDTO.builder()
                            .id(characteristicId)
                            .values(rangeValues)
                            .build());
                }

                if (entry.getValue().isSterms()) {
                    List<String> textValues = new ArrayList<>();
                    for (var agg : entry.getValue().sterms().buckets().array()) {
                        textValues.add(agg.key().stringValue());
                    }

                    if (textValues.isEmpty()) continue;
                    filtersDTO.getTextFilters().add(TextFilterDTO.builder()
                            .id(characteristicId)
                            .values(textValues)
                            .build());
                }
            }
        }

        return filtersDTO;
    }

    private List<ProductDTO> getContent(SearchResponse<Product> response) {
        return response.hits().hits()
                .stream()
                .filter(doc -> doc.source() != null)
                .map(Hit::source)
                .map(searchMapper::toProductDTO)
                .toList();
    }

    private List<Object> getSearchAfter(SearchResponse<Product> response) {
        var hits = response.hits().hits();

        if (hits.isEmpty()) {
            return Collections.emptyList();
        }

        return hits.get(hits.size() - 1).sort()
                .stream()
                .map(FieldValue::_get)
                .toList();
    }
}