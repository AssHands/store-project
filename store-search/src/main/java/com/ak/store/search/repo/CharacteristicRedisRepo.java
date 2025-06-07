package com.ak.store.search.repo;


import com.ak.store.common.document.catalogue.CharacteristicDocument;

import java.util.List;

public interface CharacteristicRedisRepo {
    List<CharacteristicDocument> findAllCharacteristicByCategoryId(Long categoryId);
}
