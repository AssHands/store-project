package com.ak.store.synchronization.repository.redis;


import com.ak.store.synchronization.model.document.Characteristic;

public interface CharacteristicRedisRepo {
    void saveOne(Characteristic characteristic);

    void deleteOne(Long id);
}
