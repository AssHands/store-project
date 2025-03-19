package com.ak.store.synchronization.repo.redis;

import com.ak.store.synchronization.model.document.ProductDocument;
import org.springframework.data.repository.CrudRepository;

public interface ProductRedisRepo extends CrudRepository<ProductDocument, Long> {
}
