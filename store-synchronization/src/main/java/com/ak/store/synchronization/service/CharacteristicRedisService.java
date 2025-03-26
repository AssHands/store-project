package com.ak.store.synchronization.service;

import com.ak.store.common.model.catalogue.document.CharacteristicDocument;
import com.ak.store.common.model.catalogue.dto.CharacteristicDTO;
import com.ak.store.synchronization.repo.redis.CharacteristicRedisRepo;
import com.ak.store.synchronization.util.mapper.CharacteristicMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacteristicRedisService {
    private final CharacteristicRedisRepo characteristicRedisRepo;
    private final CharacteristicMapper characteristicMapper;

    public List<CharacteristicDocument> createAll(List<CharacteristicDTO> characteristics) {
        return characteristicRedisRepo.saveAll(characteristicMapper.toCharacteristicDocument(characteristics));
    }

    public List<CharacteristicDocument> updateAll(List<CharacteristicDTO> characteristics) {
        return characteristicRedisRepo.saveAll(characteristicMapper.toCharacteristicDocument(characteristics));
    }

    public void deleteAll(List<Long> ids) {
        characteristicRedisRepo.deleteAllById(ids);
    }
}
