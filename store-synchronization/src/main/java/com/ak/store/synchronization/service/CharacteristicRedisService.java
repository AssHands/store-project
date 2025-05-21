package com.ak.store.synchronization.service;

import com.ak.store.common.model.catalogue.document.CharacteristicDocument;
import com.ak.store.common.model.catalogue.snapshot.CharacteristicSnapshotPayload;
import com.ak.store.synchronization.repo.redis.CharacteristicRedisRepo;
import com.ak.store.synchronization.mapper.CharacteristicMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacteristicRedisService {
    private final CharacteristicRedisRepo characteristicRedisRepo;
    private final CharacteristicMapper characteristicMapper;

    public List<CharacteristicDocument> createAll(List<CharacteristicSnapshotPayload> request) {
        return characteristicRedisRepo.saveAll(characteristicMapper.toCharacteristicDocument(request));
    }

    public List<CharacteristicDocument> updateAll(List<CharacteristicSnapshotPayload> request) {
        return characteristicRedisRepo.saveAll(characteristicMapper.toCharacteristicDocument(request));
    }

    public void deleteAll(List<Long> ids) {
        characteristicRedisRepo.deleteAllById(ids);
    }
}
