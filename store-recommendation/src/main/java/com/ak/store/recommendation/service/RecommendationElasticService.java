package com.ak.store.recommendation.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
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
import java.util.Set;

@RequiredArgsConstructor
@Service
public class RecommendationElasticService {
    private final ElasticsearchClient esClient;
    private final ProductMapper productMapper;
    private final int SIZE = 20;

    public RecommendationResponse getRecommendation(List<Long> categoryIds) {
        RecommendationResponse recommendationResponse = new RecommendationResponse();
        List<Query> filters = new ArrayList<>();
        for (Long categoryId : categoryIds) {
            filters.add(TermQuery.of(t -> t
                            .field("category_id")
                            .value(categoryId))
                    ._toQuery());
        }

        recommendationResponse.getContent().addAll(
                getContent(
                        sendRequest(
                                buildSearchRequest(
                                        buildBoolQuery(filters)
                                )))
        );

        if (recommendationResponse.getContent().size() >= SIZE) {
            return recommendationResponse;
        }

        int size = SIZE - recommendationResponse.getContent().size();
        List<Long> excludingIds = new ArrayList<>(
                recommendationResponse.getContent().stream().map(ProductPoorView::getId).toList()
        );

        recommendationResponse.getContent().addAll(
                getContent(
                        sendRequest(
                                buildSearchRequest(
                                        buildBoolQueryExcludingIds(excludingIds), size
                                )))
        );

        return recommendationResponse;
    }

    private BoolQuery buildBoolQuery(List<Query> filters) {
        return BoolQuery.of(b -> {
            if (filters != null && !filters.isEmpty()) {
                b.should(filters);
            }

            return b;
        });
    }

    private BoolQuery buildBoolQueryExcludingIds(List<Long> ids) {
        return BoolQuery.of(b -> {
            for (Long id : ids) {
                b.mustNot(TermQuery.of(t -> t
                                .field("id")
                                .value(id))
                        ._toQuery()
                );
            }

            return b;
        });
    }

    private SearchRequest buildSearchRequest(BoolQuery boolQuery) {
        return SearchRequest.of(sr -> {
            sr
                    .index("product")
                    .query(q -> q.bool(boolQuery))
                    .size(SIZE);

            sr.sort(s -> s.field(sort -> sort.field("current_price")));

            return sr;
        });
    }

    private SearchRequest buildSearchRequest(BoolQuery boolQuery, int size) {
        return SearchRequest.of(sr -> {
            sr
                    .index("product")
                    .query(q -> q.bool(boolQuery))
                    .size(size);

            sr.sort(s -> s.field(sort -> sort.field("current_price")));

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
