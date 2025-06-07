package com.ak.store.search.repo;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.ak.store.common.document.catalogue.ProductDocument;

public interface ElasticRepo {
    SearchResponse<ProductDocument> findAllProduct(SearchRequest request);

    Long defineCategory(SearchRequest request);
}
