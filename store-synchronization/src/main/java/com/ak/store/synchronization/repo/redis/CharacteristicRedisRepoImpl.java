package com.ak.store.synchronization.repo.redis;

import com.ak.store.common.model.catalogue.document.CharacteristicDocument;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CharacteristicRedisRepoImpl implements CharacteristicRedisRepo {

    private final StringRedisTemplate stringRedisTemplate;

    private static final Gson gson = new Gson();

    @Override
    public CharacteristicDocument save(CharacteristicDocument characteristic) {
        stringRedisTemplate.opsForValue().set("characteristic:" + characteristic.getId(), gson.toJson(characteristic));
        return characteristic;
    }

    @Override
    public List<CharacteristicDocument> saveAll(List<CharacteristicDocument> characteristics) {
        for (CharacteristicDocument characteristic : characteristics) {
            stringRedisTemplate.opsForValue().set("characteristic:" + characteristic.getId(), gson.toJson(characteristic));
        }
        return characteristics;
    }

    @Override
    public void deleteById(Long id) {
        stringRedisTemplate.delete("characteristic:" + id);
    }

    @Override
    public void deleteAllById(List<Long> ids) {
        for (Long id : ids) {
            stringRedisTemplate.delete("characteristic:" + id);
        }
    }
}
