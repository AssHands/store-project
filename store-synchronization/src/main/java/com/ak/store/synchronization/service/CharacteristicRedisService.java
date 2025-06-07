package com.ak.store.synchronization.service;

import com.ak.store.common.snapshot.catalogue.CharacteristicSnapshotPayload;
import com.ak.store.synchronization.repo.redis.CharacteristicRedisRepo;
import com.ak.store.synchronization.mapper.CharacteristicMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CharacteristicRedisService {
    private final CharacteristicRedisRepo characteristicRedisRepo;
    private final CharacteristicMapper characteristicMapper;

    public void createOne(CharacteristicSnapshotPayload request) {
        characteristicRedisRepo.saveOne(characteristicMapper.toCharacteristicDocument(request));
    }

    public void updateOne(CharacteristicSnapshotPayload request) {
        characteristicRedisRepo.saveOne(characteristicMapper.toCharacteristicDocument(request));
    }

    public void deleteOne(Long id) {
        characteristicRedisRepo.deleteOne(id);
    }
}
