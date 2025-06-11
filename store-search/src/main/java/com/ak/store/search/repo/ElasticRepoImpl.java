package com.ak.store.search.repo;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.ak.store.search.model.document.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@RequiredArgsConstructor
@Repository
public class ElasticRepoImpl implements ElasticRepo {
    private final ElasticsearchClient esClient;

    @Override
    public SearchResponse<Product> findAllProduct(SearchRequest request) {
        return sendRequest(request);
    }

    @Override
    public Long defineCategory(SearchRequest request) {
        var aggs = request.aggregations().keySet();

        if (aggs.size() != 1) {
            throw new RuntimeException("your request must have only 1 agg");
        }

        String aggName = aggs.iterator().next();
        SearchResponse<Product> response = sendRequest(request);

        var mostFrequentCategory = response.aggregations().get(aggName).sterms().buckets().array();

        try {
            return Long.parseLong(mostFrequentCategory.get(0).key().stringValue());
        } catch (Exception e) {
            throw new RuntimeException("can not define category by text");
        }
    }

    private SearchResponse<Product> sendRequest(SearchRequest request) {
        try {
            return esClient.search(request, Product.class);
        } catch (IOException e) {
            throw new RuntimeException("defineCategory error");
        }
    }
}