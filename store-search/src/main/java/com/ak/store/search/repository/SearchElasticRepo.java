package com.ak.store.search.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.ak.store.search.model.command.SearchFilterCommand;
import com.ak.store.search.model.command.SearchProductCommand;
import com.ak.store.search.model.document.Product;
import com.ak.store.search.repository.builder.AggregationBuilder;
import com.ak.store.search.repository.builder.FilterBuilder;
import com.ak.store.search.repository.builder.QueryBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SearchElasticRepo implements SearchRepo {
    private final ElasticsearchClient esClient;

    private final QueryBuilder queryBuilder;
    private final FilterBuilder filterBuilder;
    private final AggregationBuilder aggregationBuilder;

    //todo что делать, если репозиторий обращается к другому репозиторию? исправить
    private final CharacteristicRepo characteristicRepo;

    @Override
    public SearchResponse<Product> searchAllProduct(SearchProductCommand command) {
        List<Query> filters = filterBuilder.buildFiltersFromDTO(command);

        return sendRequest(
                queryBuilder.buildProductRequest(command,
                        queryBuilder.buildBoolQuery(filters, command.getText())
                )
        );
    }

    @Override
    public SearchResponse<Product> searchAllFilter(SearchFilterCommand command) {
        List<Query> filters = filterBuilder.buildFiltersFromDTO(command);

        return sendRequest(
                queryBuilder.buildFilterRequest(
                        queryBuilder.buildBoolQuery(filters, command.getText()),
                        aggregationBuilder.buildAggregations(
                                command,
                                characteristicRepo.findAllWithValuesByCategoryId(command.getCategoryId())
                        )
                )
        );
    }

    @Override
    public Optional<Long> defineCategory(String text) {
        var request = queryBuilder.buildDefineCategoryRequest(text);

        var aggs = request.aggregations().keySet();
        String aggName = aggs.iterator().next();

        SearchResponse<Product> response = sendRequest(request);
        var mostFrequentCategory = response.aggregations().get(aggName).sterms().buckets().array();

        try {
            return Optional.of(Long.parseLong(mostFrequentCategory.get(0).key().stringValue()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private SearchResponse<Product> sendRequest(SearchRequest request) {
        try {
            return esClient.search(request, Product.class);
        } catch (IOException e) {
            throw new RuntimeException("error");
        }
    }
}