package com.ak.store.synchronization.redis;

import com.ak.store.synchronization.model.document.CharacteristicDocument;
import org.springframework.data.repository.CrudRepository;

public interface CharacteristicRedisRepo extends CrudRepository<CharacteristicDocument, Long> {
}
