package com.ak.store.synchronization.repo.redis;


import com.ak.store.common.document.catalogue.CharacteristicDocument;

public interface CharacteristicRedisRepo {
    void saveOne(CharacteristicDocument characteristic);

    void deleteOne(Long id);
}
