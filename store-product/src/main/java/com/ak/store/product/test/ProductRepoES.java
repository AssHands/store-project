package com.ak.store.product.test;

import com.ak.store.common.document.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductRepoES extends ElasticsearchRepository<ProductDocument, Long> {

}
