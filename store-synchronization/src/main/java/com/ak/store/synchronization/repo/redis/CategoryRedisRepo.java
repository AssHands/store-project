package com.ak.store.synchronization.repo.redis;

import com.ak.store.synchronization.model.document.CategoryDocument;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRedisRepo extends CrudRepository<CategoryDocument, Long> {
}
