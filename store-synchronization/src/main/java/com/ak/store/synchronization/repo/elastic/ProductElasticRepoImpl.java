package com.ak.store.synchronization.repo.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import com.ak.store.common.document.catalogue.ProductDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductElasticRepoImpl implements ProductElasticRepo {
    private final ElasticsearchClient esClient;

    @Value("${elasticsearch.indexes.product}")
    private String PRODUCT_INDEX;

    @Override
    public void saveOne(ProductDocument productDocument) {
        var request = IndexRequest.of(i -> i
                .index(PRODUCT_INDEX)
                .id(productDocument.getId().toString())
                .document(productDocument));

        try {
            esClient.index(request);
        } catch (Exception e) {
            throw new RuntimeException("save error");
        }
    }

    @Override
    public void updateOne(ProductDocument productDocument) {
        var request = UpdateRequest.of(u -> u
                .index(PRODUCT_INDEX)
                .id(productDocument.getId().toString())
                .doc(productDocument));

        try {
            esClient.update(request, ProductDocument.class);
        } catch (Exception e) {
            throw new RuntimeException("update one error");
        }
    }

    @Override
    public void deleteOne(Long id) {
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
