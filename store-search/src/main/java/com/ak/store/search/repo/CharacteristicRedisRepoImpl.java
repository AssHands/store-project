package com.ak.store.search.repo;

import com.ak.store.common.model.catalogue.document.CharacteristicDocument;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class CharacteristicRedisRepoImpl implements CharacteristicRedisRepo {
    private final StringRedisTemplate stringRedisTemplate;
    private static final Gson gson = new Gson();

    @Override
    public List<CharacteristicDocument> findAllCharacteristicByCategoryId(Long id) {
        var set = stringRedisTemplate.opsForSet().members("category_characteristic:" + id);

        if (set == null) {
            return Collections.emptyList();
        }

        List<String> keys = new ArrayList<>();
        for (var entry : set) {
            keys.add("characteristic:" + entry);
        }

        var list = stringRedisTemplate.opsForValue().multiGet(keys);

        if (list == null) {
            return Collections.emptyList();
        }

        List<CharacteristicDocument> values = new ArrayList<>();
        for(var entry : list) {
            values.add(gson.fromJson(entry, CharacteristicDocument.class));
        }

        return values;
    }
}
