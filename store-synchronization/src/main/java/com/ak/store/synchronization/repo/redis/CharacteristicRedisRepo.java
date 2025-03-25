package com.ak.store.synchronization.repo.redis;


import com.ak.store.common.model.catalogue.document.CharacteristicDocument;

import java.util.List;

public interface CharacteristicRedisRepo {
    CharacteristicDocument save(CharacteristicDocument characteristic);

    List<CharacteristicDocument> saveAll(List<CharacteristicDocument> characteristics);

    List<CharacteristicDocument> updateAll(List<CharacteristicDocument> characteristics);

    void deleteById(Long id);

    void deleteAllById(List<Long> ids);
}
