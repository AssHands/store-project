package com.ak.store.search.repo;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.ak.store.common.document.catalogue.ProductDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@RequiredArgsConstructor
@Repository
public class ElasticRepoImpl implements ElasticRepo {
    private final ElasticsearchClient esClient;

    @Override
    public SearchResponse<ProductDocument> findAllProduct(SearchRequest request) {
        try {
            return esClient.search(request, ProductDocument.class);
        } catch (IOException e) {
            throw new RuntimeException("findAllProduct error");
        }
    }

    @Override
    public Long defineCategory(SearchRequest request) {
        var aggs = request.aggregations().keySet();

        if(aggs.size() != 1) {
            throw new RuntimeException("your request have more than 1 agg");
        }

        String aggName = aggs.iterator().next();
        SearchResponse<ProductDocument> response;

        try {
            response = esClient.search(request, ProductDocument.class);
        } catch (IOException e) {
            throw new RuntimeException("defineCategory error");
        }

        var mostFrequentCategory = response.aggregations().get(aggName).sterms().buckets().array();

        try {
            return Long.parseLong(mostFrequentCategory.get(0).key().stringValue());
        } catch (Exception e) {
            throw new RuntimeException("can not define category by text");
        }
    }
}