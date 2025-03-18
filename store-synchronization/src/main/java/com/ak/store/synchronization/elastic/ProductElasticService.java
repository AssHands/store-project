package com.ak.store.synchronization.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.ak.store.synchronization.model.document.ProductDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductElasticService {
    private final ElasticsearchClient esClient;

    public void compensateDeleteOne(ProductDocument productDocument) {
        var request = IndexRequest.of(i -> i
                .index("product")
                .id(productDocument.getId().toString())
                .document(productDocument));

        try {
            esClient.index(request);
        } catch (Exception e) {
            throw new RuntimeException("compensate index document error");
        }
    }

    public void compensateCreateOne(Long id) {
        var request = DeleteRequest.of(d -> d
                .index("product")
                .id(id.toString()));

        try {
            esClient.delete(request);
        } catch (Exception e) {
            throw new RuntimeException("compensate delete document error");
        }
    }

    public void createOne(ProductDocument productDocument) {
        var request = IndexRequest.of(i -> i
                 .index("product")
                 .id(productDocument.getId().toString())
                 .document(productDocument));

        try {
            esClient.index(request);
        } catch (Exception e) {
            throw new RuntimeException("index document error");
        }
    }

    public void createAll(List<ProductDocument> productDocuments) {
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
            throw new RuntimeException("bulk document error");
        }

        if (result.errors()) {
            for (BulkResponseItem item: result.items()) {
                if (item.error() != null) {
                    throw new RuntimeException("bulk had errors\n" + item.error().reason());
                }
            }
        }
    }

    public void updateOne(ProductDocument productDocument) {
        var request = UpdateRequest.of(u -> u
                .index("product")
                .id(productDocument.getId().toString())
                .doc(productDocument));

        try {
            esClient.update(request, ProductDocument.class);
        } catch (Exception e) {
            throw new RuntimeException("update document error");
        }
    }

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
            throw new RuntimeException("bulk document error");
        }

        if (result.errors()) {
            for (BulkResponseItem item: result.items()) {
                if (item.error() != null) {
                    throw new RuntimeException("bulk had errors\n" + item.error().reason());
                }
            }
        }
    }

    public void deleteOne(Long id) {
        var request = DeleteRequest.of(d -> d
                .index("product")
                .id(id.toString()));

        try {
            esClient.delete(request);
        } catch (Exception e) {
            throw new RuntimeException("delete document error");
        }
    }

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
            throw new RuntimeException("bulk document error");
        }

        if (result.errors()) {
            for (BulkResponseItem item: result.items()) {
                if (item.error() != null) {
                    throw new RuntimeException("bulk had errors\n" + item.error().reason());
                }
            }
        }
    }
}