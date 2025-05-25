package com.ak.store.synchronization.repo.redis;


import com.ak.store.common.model.catalogue.document.CharacteristicDocument;

import java.util.List;

public interface CharacteristicRedisRepo {
    void saveOne(CharacteristicDocument characteristic);

    void deleteOne(Long id);
}
