package com.ak.store.search.redis;

import com.ak.store.search.model.document.CategoryDocument;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRedisRepo extends CrudRepository<CategoryDocument, Long> {
}
