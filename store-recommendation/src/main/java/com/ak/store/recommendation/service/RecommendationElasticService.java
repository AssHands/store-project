package com.ak.store.recommendation.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.ak.store.common.model.catalogue.document.ProductDocument;
import com.ak.store.common.model.catalogue.view.ProductPoorView;
import com.ak.store.common.model.recommendation.RecommendationResponse;
import com.ak.store.recommendation.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RecommendationElasticService {
    private final ElasticsearchClient esClient;
    private final ProductMapper productMapper;
    private final int SIZE = 20;

    public RecommendationResponse getRecommendation() {
        RecommendationResponse recommendationResponse = new RecommendationResponse();

        recommendationResponse.getContent().addAll(
                getContent(
                        sendRequest(
                                buildSearchRequest()
                        )
                )
        );

        return recommendationResponse;
    }

    public RecommendationResponse getRecommendation(List<Long> categoryIds) {
        RecommendationResponse recommendationResponse = new RecommendationResponse();

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

        recommendationResponse.getContent().addAll(
                getContent(
                        sendRequest(
                                buildSearchRequest(
                                        buildBoolQuery(filters)
                                )
                        )
                )
        );

        return recommendationResponse;
    }

    private BoolQuery buildBoolQuery(List<Query> filters) {
        return BoolQuery.of(b -> {
            if (filters != null && !filters.isEmpty()) {
                b.filter(filters);
            }

            return b;
        });
    }

    private SearchRequest buildSearchRequest(BoolQuery boolQuery) {
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
                    .size(SIZE);

            return sr;
        });
    }

    private SearchRequest buildSearchRequest() {
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
                    .size(SIZE);

            return sr;
        });
    }

    private SearchResponse<ProductDocument> sendRequest(SearchRequest searchRequest) {
        try {
            return esClient.search(searchRequest, ProductDocument.class);
        } catch (IOException e) {
            throw new RuntimeException("send request error");
        }
    }

    private List<ProductPoorView> getContent(SearchResponse<ProductDocument> searchResponse) {
        return searchResponse.hits().hits().stream()
                .filter(doc -> doc.source() != null)
                .map(Hit::source)
                .map(productMapper::toProductPoorView)
                .toList();
    }
}
