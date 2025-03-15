package com.ak.store.synchronization.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.ak.store.common.document.CharacteristicDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacteristicElasticService {
    private final ElasticsearchClient esClient;

    public void deleteOne(Long id) {
        var request = DeleteRequest.of(d -> d
                .index("characteristic")
                .id(id.toString()));

        try {
            esClient.delete(request);
        } catch (Exception e) {
            throw new RuntimeException("delete document error");
        }
    }

    public void createOne(CharacteristicDocument characteristicDocument) {
        var request = IndexRequest.of(i -> i
                .index("characteristic")
                .id(characteristicDocument.getId().toString())
                .document(characteristicDocument));

        try {
            esClient.index(request);
        } catch (Exception e) {
            throw new RuntimeException("index document error");
        }
    }

    public void updateOne(CharacteristicDocument characteristicDocument) {
        var request = UpdateRequest.of(u -> u
                .index("characteristic")
                .id(characteristicDocument.getId().toString())
                .doc(characteristicDocument));

        try {
            esClient.update(request, CharacteristicDocument.class);
        } catch (Exception e) {
            throw new RuntimeException("update document error");
        }
    }

    public void createAll(List<CharacteristicDocument> characteristicDocuments) {
        var br = new BulkRequest.Builder();

        characteristicDocuments.forEach(characteristic ->
                br.operations(op -> op
                        .index(idx -> idx
                                .index("characteristic")
                                .id(characteristic.getId().toString())
                                .document(characteristic))));

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

    public void updateAll(List<CharacteristicDocument> characteristicDocuments) {
        var br = new BulkRequest.Builder();

        characteristicDocuments.forEach(characteristic ->
                br.operations(op -> op
                        .index(idx -> idx
                                .index("characteristic")
                                .id(characteristic.getId().toString())
                                .document(characteristic))));

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

    public void deleteAll(List<Long> ids) {
        var br = new BulkRequest.Builder();

        ids.forEach(id ->
                br.operations(op -> op
                        .delete(idx -> idx
                                .index("characteristic")
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
