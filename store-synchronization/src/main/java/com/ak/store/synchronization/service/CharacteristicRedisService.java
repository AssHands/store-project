package com.ak.store.synchronization.service;

import com.ak.store.synchronization.model.document.CharacteristicDocument;
import com.ak.store.synchronization.repo.redis.CharacteristicRedisRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacteristicRedisService {
    private final CharacteristicRedisRepo characteristicRedisRepo;

    public CharacteristicDocument createOne(CharacteristicDocument characteristic) {
        return characteristicRedisRepo.save(characteristic);
    }

    public List<CharacteristicDocument> createAll(List<CharacteristicDocument> characteristics) {
        return (List<CharacteristicDocument>) characteristicRedisRepo.saveAll(characteristics);
    }

    public CharacteristicDocument updateOne(CharacteristicDocument characteristic) {
        return characteristicRedisRepo.save(characteristic);
    }

    public List<CharacteristicDocument> updateAll(List<CharacteristicDocument> characteristics) {
        return (List<CharacteristicDocument>) characteristicRedisRepo.saveAll(characteristics);
    }

    public void deleteOne(Long id) {
        characteristicRedisRepo.deleteById(id);
    }

    public void deleteAll(List<Long> ids) {
        characteristicRedisRepo.deleteAllById(ids);
    }
}
