package com.ak.store.catalogue.test;

import com.ak.store.catalogue.model.document.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductRepoES extends ElasticsearchRepository<ProductDocument, Long> {

}
