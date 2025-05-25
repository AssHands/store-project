package com.ak.store.search.repo;

import com.ak.store.common.model.catalogue.document.CharacteristicDocument;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


@Repository
@RequiredArgsConstructor
public class CharacteristicRedisRepoImpl implements CharacteristicRedisRepo {
    private final StringRedisTemplate stringRedisTemplate;
    private final Gson gson;

    //todo refactor
    private final String CHARACTERISTIC_KEY = "characteristic:";
    private final String CATEGORY_CHARACTERISTIC_KEY = "category_characteristic:";

    @Override
    public List<CharacteristicDocument> findAllCharacteristicByCategoryId(Long categoryId) {
        Set<String> characteristicIds = stringRedisTemplate.opsForSet().members(CATEGORY_CHARACTERISTIC_KEY + categoryId);

        if (characteristicIds == null) {
            return Collections.emptyList();
        }

        List<String> keys = new ArrayList<>();
        for (var characteristicId : characteristicIds) {
            keys.add(CHARACTERISTIC_KEY + characteristicId);
        }

        List<String> characteristics = stringRedisTemplate.opsForValue().multiGet(keys);

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
