package com.ak.store.catalogue.model.pojo;

import com.ak.store.common.document.ProductDocument;
import com.ak.store.common.model.search.common.SortingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElasticSearchResult {
    @Builder.Default
    List<ProductDocument> content = new ArrayList<>();
    List<Object> searchAfter;
}