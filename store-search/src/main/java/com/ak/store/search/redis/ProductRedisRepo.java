package com.ak.store.search.redis;

import com.ak.store.search.model.document.ProductDocument;
import org.springframework.data.repository.CrudRepository;

public interface ProductRedisRepo extends CrudRepository<ProductDocument, Long> {
}
