package com.ak.store.search.repo;


import com.ak.store.search.model.document.Characteristic;

import java.util.List;

public interface CharacteristicRedisRepo {
    List<Characteristic> findAllByCategoryId(Long categoryId);
}