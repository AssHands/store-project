package com.ak.store.synchronization.repo.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.ak.store.common.model.catalogue.document.ProductDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductElasticRepoImpl implements ProductElasticRepo {
    private final ElasticsearchClient esClient;


    @Override
    public void saveOne(ProductDocument productDocument) {
        var request = IndexRequest.of(i -> i
                .index("product")
                .id(productDocument.getId().toString())
                .document(productDocument));

        try {
            esClient.index(request);
        } catch (Exception e) {
            throw new RuntimeException("save error");
        }
    }

    @Override
    public void saveAll(List<ProductDocument> productDocuments) {
        var br = new BulkRequest.Builder();

        productDocuments.forEach(product ->
                br.operations(op -> op
                        .index(idx -> idx
                                .index("product")
                                .id(product.getId().toString())
                                .document(product))));

        BulkResponse result = null;

        try {
            result = esClient.bulk(br.build());
        } catch (Exception e) {
            throw new RuntimeException("save all error");
        }

        if (result.errors()) {
            for (BulkResponseItem item: result.items()) {
                if (item.error() != null) {
                    throw new RuntimeException("save all had errors\n" + item.error().reason());
                }
            }
        }
    }

    @Override
    public void updateOne(ProductDocument productDocument) {
        var request = UpdateRequest.of(u -> u
                .index("product")
                .id(productDocument.getId().toString())
                .doc(productDocument));

        try {
            esClient.update(request, ProductDocument.class);
        } catch (Exception e) {
            throw new RuntimeException("update one error");
        }
    }

    @Override
    public void updateAll(List<ProductDocument> productDocuments) {
        var br = new BulkRequest.Builder();

        productDocuments.forEach(product ->
                br.operations(op -> op
                        .index(idx -> idx
                                .index("product")
                                .id(product.getId().toString())
                                .document(product))));

        BulkResponse result = null;

        try {
            result = esClient.bulk(br.build());
        } catch (Exception e) {
            throw new RuntimeException("bulk all error");
        }

        if (result.errors()) {
            for (BulkResponseItem item: result.items()) {
                if (item.error() != null) {
                    throw new RuntimeException("bulk all had errors\n" + item.error().reason());
                }
            }
        }
    }

    @Override
    public void deleteOne(Long id) {
        var request = DeleteRequest.of(d -> d
                .index("product")
                .id(id.toString()));

        try {
            esClient.delete(request);
        } catch (Exception e) {
            throw new RuntimeException("delete one error");
        }
    }

    @Override
    public void deleteAll(List<Long> ids) {
        var br = new BulkRequest.Builder();

        ids.forEach(id ->
                br.operations(op -> op
                        .delete(idx -> idx
                                .index("product")
                                .id(id.toString()))));

        BulkResponse result = null;

        try {
            result = esClient.bulk(br.build());
        } catch (Exception e) {
            throw new RuntimeException("bulk all error");
        }

        if (result.errors()) {
            for (BulkResponseItem item: result.items()) {
                if (item.error() != null) {
                    throw new RuntimeException("delete all had errors\n" + item.error().reason());
                }
            }
        }
    }
}
