package com.ak.store.search.redis;

import com.ak.store.search.model.document.CharacteristicDocument;
import org.springframework.data.repository.CrudRepository;

public interface CharacteristicRedisRepo extends CrudRepository<CharacteristicDocument, Long> {
}
