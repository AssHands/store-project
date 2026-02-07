package com.ak.store.recommendation.repo.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.ak.store.recommendation.model.document.Product;
import com.ak.store.recommendation.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductElasticRepo implements ProductRepo {
    private final ElasticsearchClient esClient;

    @Override
    public List<Product> findRandomByCategoryIds(List<Long> categoryIds, int size) {
        return getContent(
                sendRequest(
                        buildSearchRequest(
                                buildBoolQuery(
                                        buildCategoryFilters(categoryIds)
                                ), size
                        )
                )
        );
    }

    @Override
    public List<Product> findRandom(int size) {
        return getContent(
                sendRequest(
                        buildSearchRequest(size)
                )
        );
    }

    private List<Query> buildCategoryFilters(List<Long> categoryIds) {
        List<Query> filters = new ArrayList<>();

        List<FieldValue> fieldValues = categoryIds.stream()
                .map(FieldValue::of)
                .toList();

        TermsQueryField termsQueryField = TermsQueryField.of(t -> t.value(fieldValues));
        filters.add(TermsQuery.of(tq -> tq
                        .field("category_id")
                        .terms(termsQueryField))
                ._toQuery()
        );

        return filters;
    }

    private BoolQuery buildBoolQuery(List<Query> filters) {
        return BoolQuery.of(b -> {
            if (filters != null && !filters.isEmpty()) {
                b.filter(filters);
            }

            return b;
        });
    }

    private SearchRequest buildSearchRequest(BoolQuery boolQuery, int size) {
        return SearchRequest.of(sr -> {
            sr
                    .index("product")
                    .query(q -> q
                            .functionScore(FunctionScoreQuery.of(fs -> fs
                                            .query(q2 -> q2
                                                    .bool(boolQuery))
                                            .functions(f -> f
                                                    .randomScore(rs -> rs
                                                            .field("id")
                                                            .seed(String.valueOf(System.currentTimeMillis()))
                                                    )
                                            )
                                            .boostMode(FunctionBoostMode.Replace)
                                    )
                            )
                    )
                    .size(size);

            return sr;
        });
    }

    private SearchRequest buildSearchRequest(int size) {
        return SearchRequest.of(sr -> {
            sr
                    .index("product")
                    .query(q -> q
                            .functionScore(FunctionScoreQuery.of(fs -> fs
                                            .functions(f -> f
                                                    .randomScore(rs -> rs
                                                            .field("id")
                                                            .seed(String.valueOf(System.currentTimeMillis()))
                                                    )
                                            )
                                            .boostMode(FunctionBoostMode.Replace)
                                    )
                            )
                    )
                    .size(size);

            return sr;
        });
    }

    private SearchResponse<Product> sendRequest(SearchRequest searchRequest) {
        try {
            return esClient.search(searchRequest, Product.class);
        } catch (IOException e) {
            throw new RuntimeException("send request error");
        }
    }

    private List<Product> getContent(SearchResponse<Product> searchResponse) {
        return searchResponse.hits().hits().stream()
                .filter(doc -> doc.source() != null)
                .map(Hit::source)
                .toList();
    }
}
