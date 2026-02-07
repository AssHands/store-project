package com.ak.store.search.repository;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.ak.store.search.model.command.SearchFilterCommand;
import com.ak.store.search.model.command.SearchProductCommand;
import com.ak.store.search.model.document.Product;

import java.util.Optional;

public interface SearchRepo {
    SearchResponse<Product> searchAllProduct(SearchProductCommand command);

    SearchResponse<Product> searchAllFilter(SearchFilterCommand command);

    Optional<Long> defineCategory(String text);
}
