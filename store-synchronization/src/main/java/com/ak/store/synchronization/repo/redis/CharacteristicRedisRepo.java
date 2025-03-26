package com.ak.store.synchronization.repo.redis;


import com.ak.store.common.model.catalogue.document.CharacteristicDocument;

import java.util.List;

public interface CharacteristicRedisRepo {
    CharacteristicDocument saveOne(CharacteristicDocument characteristic);

    List<CharacteristicDocument> saveAll(List<CharacteristicDocument> characteristics);

    void deleteOneById(Long id);

    void deleteAllById(List<Long> ids);
}
