package com.ak.store.search.redis;

import org.springframework.data.repository.CrudRepository;

public interface CategoryRedisRepo extends CrudRepository<CategoryDocument, Long> {
}
