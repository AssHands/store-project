package com.ak.store.search.service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.ak.store.search.mapper.SearchMapper;
import com.ak.store.search.model.document.Product;
import com.ak.store.search.model.dto.*;
import com.ak.store.search.model.dto.request.FilterSearchRequestDTO;
import com.ak.store.search.model.dto.request.ProductSearchRequestDTO;
import com.ak.store.search.model.dto.response.FilterSearchResponseDTO;
import com.ak.store.search.model.dto.response.ProductSearchResponseDTO;
import com.ak.store.search.repo.CharacteristicRedisRepo;
import com.ak.store.search.repo.ElasticRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class SearchElasticService {
    private final CharacteristicRedisRepo characteristicRedisRepo;
    private final SearchMapper searchMapper;
    private final ElasticRepo elasticRepo;

    private final QueryBuilder queryBuilder;
    private final FilterBuilder filterBuilder;
    private final AggregationBuilder aggregationBuilder;


    public FilterSearchResponseDTO searchAllFilter(FilterSearchRequestDTO request) {
        if (request.getCategoryId() == null) {
            //todo ловить ошибку в случае неудачи определения категории и возвращать пустые фильтры
            request.setCategoryId(
                    elasticRepo.defineCategory(queryBuilder.buildDefineCategoryRequest(request.getText()))
            );
        }

        List<Query> filters = filterBuilder.buildFiltersFromDTO(request);

        var response = elasticRepo.findAllProduct(
                queryBuilder.buildFilterRequest(
                        queryBuilder.buildBoolQuery(filters, request.getText()),
                        aggregationBuilder.buildAggregations(request,
                                characteristicRedisRepo.findAllByCategoryId(request.getCategoryId())
                        )
                )
        );

        return FilterSearchResponseDTO.builder()
                .filters(getFiltersDTO(response))
                .categoryId(request.getCategoryId())
                .build();
    }

    public ProductSearchResponseDTO searchAllProduct(ProductSearchRequestDTO request) {
        if (request.getCategoryId() == null) {
            //todo ловить ошибку в случае неудачи определения категории и возвращать пустые фильтры
            request.setCategoryId(
                    elasticRepo.defineCategory(queryBuilder.buildDefineCategoryRequest(request.getText()))
            );
        }

        List<Query> filters = filterBuilder.buildFiltersFromDTO(request);

        var response = elasticRepo.findAllProduct(
                queryBuilder.buildProductRequest(request,
                        queryBuilder.buildBoolQuery(filters, request.getText())
                )
        );

        return ProductSearchResponseDTO.builder()
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