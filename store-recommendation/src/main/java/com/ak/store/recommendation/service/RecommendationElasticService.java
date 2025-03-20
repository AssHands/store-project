package com.ak.store.recommendation.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.ak.store.common.model.catalogue.view.ProductPoorView;
import com.ak.store.common.model.recommendation.RecommendationResponse;
import com.ak.store.recommendation.mapper.ProductMapper;
import com.ak.store.recommendation.model.ProductDocument;
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

    public RecommendationResponse getRecommendation(Set<Long> categoryIds) {
        List<Query> filters = new ArrayList<>();

        for (Long categoryId : categoryIds) {
            filters.add(TermQuery.of(t -> t
                            .field("category_id")
                            .value(categoryId))
                    ._toQuery());
        }

        SearchRequest searchRequest = buildSearchRequest(makeSearchQueryWithFilters(filters));

        SearchResponse<ProductDocument> response;

        try {
            response = esClient.search(searchRequest, ProductDocument.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get response from ElasticSearch server");
        }

        List<Hit<ProductDocument>> productHits = response.hits().hits();

        System.out.println(searchRequest);
        response.hits().hits().forEach(System.out::println);

        RecommendationResponse recommendationResponse = new RecommendationResponse();

        recommendationResponse.getContent().addAll(
                productHits.stream()
                        .filter(doc -> doc.source() != null)
                        .map(Hit::source)
                        .map(productMapper::toProductPoorView)
                        .toList());

        if (recommendationResponse.getContent().size() >= SIZE) {
            return recommendationResponse;
        }

        List<Long> ids = new ArrayList<>(
                recommendationResponse.getContent().stream().map(ProductPoorView::getId).toList()
        );

        int size = SIZE - recommendationResponse.getContent().size();
        searchRequest = buildSearchRequest(makeSearchQueryExcludingIds(ids), size);

        try {
            response = esClient.search(searchRequest, ProductDocument.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get response from ElasticSearch server");
        }

        productHits = response.hits().hits();
        System.out.println(searchRequest);
        response.hits().hits().forEach(System.out::println);

        recommendationResponse.getContent().addAll(
                productHits.stream()
                        .filter(doc -> doc.source() != null)
                        .map(Hit::source)
                        .map(productMapper::toProductPoorView)
                        .toList());

        return recommendationResponse;
    }

    private BoolQuery makeSearchQueryWithFilters(List<Query> filters) {
        return BoolQuery.of(b -> {
            if (filters != null && !filters.isEmpty())
                b.should(filters);

            return b;
        });
    }

    private BoolQuery makeSearchQueryExcludingIds(List<Long> ids) {
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

    private SearchRequest buildSearchRequest(BoolQuery searchQuery) {
        return SearchRequest.of(sr -> {
            sr
                    .index("product")
                    .query(q -> q.bool(searchQuery))
                    .size(SIZE);

            sr.sort(s -> s.field(sort -> sort.field("current_price")));

            return sr;
        });
    }

    private SearchRequest buildSearchRequest(BoolQuery searchQuery, int size) {
        return SearchRequest.of(sr -> {
            sr
                    .index("product")
                    .query(q -> q.bool(searchQuery))
                    .size(size);

            sr.sort(s -> s.field(sort -> sort.field("current_price")));

            return sr;
        });
    }
}
