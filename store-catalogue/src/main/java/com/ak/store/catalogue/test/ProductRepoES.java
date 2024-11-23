package com.ak.store.catalogue.test;

import com.ak.store.common.document.product.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductRepoES extends ElasticsearchRepository<ProductDocument, Long> {

}
