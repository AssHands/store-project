package com.ak.store.synchronization.repository.redis;

import com.ak.store.synchronization.util.CharacteristicRedisKeys;
import com.ak.store.synchronization.model.document.Characteristic;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CharacteristicRedisRepoImpl implements CharacteristicRedisRepo {
    private final StringRedisTemplate stringRedisTemplate;
    private final Gson gson;

    @Override
    public void saveOne(Characteristic characteristic) {
        stringRedisTemplate.opsForValue().set(
                CharacteristicRedisKeys.CHARACTERISTIC + characteristic.getId(), gson.toJson(characteristic));
    }

    @Override
    public void deleteOne(Long id) {
        stringRedisTemplate.delete(CharacteristicRedisKeys.CHARACTERISTIC + id);
    }
}