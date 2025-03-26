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
    public List<CharacteristicDocument> findAllCharacteristicByCategoryId(Long categoryId) {
        var characteristicIds = stringRedisTemplate.opsForSet().members("category_characteristic:" + categoryId);

        if (characteristicIds == null) {
            return Collections.emptyList();
        }

        List<String> keys = new ArrayList<>();
        for (var characteristicId : characteristicIds) {
            keys.add("characteristic:" + characteristicId);
        }

        var characteristics = stringRedisTemplate.opsForValue().multiGet(keys);

        if (characteristics == null) {
            return Collections.emptyList();
        }

        List<CharacteristicDocument> values = new ArrayList<>();
        for(var characteristic : characteristics) {
            values.add(gson.fromJson(characteristic, CharacteristicDocument.class));
        }

        return values;
    }
}
