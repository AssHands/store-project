package com.ak.store.search.repo;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.ak.store.search.model.document.Product;

public interface ElasticRepo {
    SearchResponse<Product> findAllProduct(SearchRequest request);

    Long defineCategory(SearchRequest request);
}
