package com.ak.store.synchronization.repo.redis;

import com.ak.store.common.model.catalogue.document.CharacteristicDocument;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CharacteristicRedisRepoImpl implements CharacteristicRedisRepo {
    private final StringRedisTemplate stringRedisTemplate;
    private final Gson gson;

    private final String CHARACTERISTIC_KEY = "characteristic:";

    @Override
    public void saveOne(CharacteristicDocument characteristic) {
        stringRedisTemplate.opsForValue().set(CHARACTERISTIC_KEY + characteristic.getId(), gson.toJson(characteristic));
    }

    @Override
    public void deleteOne(Long id) {
        stringRedisTemplate.delete(CHARACTERISTIC_KEY + id);
    }
}