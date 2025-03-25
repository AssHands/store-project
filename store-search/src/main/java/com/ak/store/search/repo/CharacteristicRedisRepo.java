package com.ak.store.search.repo;


import com.ak.store.common.model.catalogue.document.CharacteristicDocument;

import java.util.List;

public interface CharacteristicRedisRepo {
    List<CharacteristicDocument> findAllCharacteristicByCategoryId(Long id);
}
