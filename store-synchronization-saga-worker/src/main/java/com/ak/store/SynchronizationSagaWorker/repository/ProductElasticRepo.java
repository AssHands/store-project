package com.ak.store.SynchronizationSagaWorker.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.ak.store.SynchronizationSagaWorker.model.document.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductElasticRepo implements ProductRepo {
    private final ElasticsearchClient esClient;

    @Value("${elasticsearch.indexes.product}")
    private String PRODUCT_INDEX;

    @Override
    public void createOne(Product product) {
        var request = IndexRequest.of(i -> i
                .index(PRODUCT_INDEX)
                .id(product.getId().toString())
                .document(product));

        try {
            esClient.index(request);
        } catch (Exception e) {
            throw new RuntimeException("save one error");
        }
    }

    @Override
    public void deleteOneById(Long id) {
        var request = DeleteRequest.of(d -> d
                .index(PRODUCT_INDEX)
                .id(id.toString()));

        try {
            esClient.delete(request);
        } catch (Exception e) {
            throw new RuntimeException("delete one error");
        }
    }
}