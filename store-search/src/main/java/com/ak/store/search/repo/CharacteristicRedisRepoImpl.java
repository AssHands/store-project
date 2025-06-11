package com.ak.store.search.repo;

import com.ak.store.search.util.CharacteristicRedisKeys;
import com.ak.store.search.model.document.Characteristic;
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

    @Override
    public List<Characteristic> findAllByCategoryId(Long categoryId) {
        Set<String> characteristicIds = stringRedisTemplate.opsForSet()
                .members(CharacteristicRedisKeys.CATEGORY_CHARACTERISTIC + categoryId);

        if (characteristicIds == null) {
            return Collections.emptyList();
        }

        List<String> keys = new ArrayList<>();
        for (var characteristicId : characteristicIds) {
            keys.add(CharacteristicRedisKeys.CHARACTERISTIC + characteristicId);
        }

        List<String> characteristics = stringRedisTemplate.opsForValue().multiGet(keys);

        if (characteristics == null) {
            return Collections.emptyList();
        }

        List<Characteristic> values = new ArrayList<>();
        for (var characteristic : characteristics) {
            values.add(gson.fromJson(characteristic, Characteristic.class));
        }

        return values;
    }
}
