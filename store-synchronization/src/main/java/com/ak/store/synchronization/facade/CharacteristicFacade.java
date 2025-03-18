package com.ak.store.synchronization.facade;

import com.ak.store.common.model.catalogue.dto.CharacteristicDTO;
import com.ak.store.synchronization.redis.CharacteristicRedisRepo;
import com.ak.store.synchronization.util.mapper.CharacteristicMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacteristicFacade {
    private final CharacteristicRedisRepo characteristicRedisRepo;
    private final CharacteristicMapper characteristicMapper;

    public void createOne(CharacteristicDTO characteristic) {
        characteristicRedisRepo.save(
                characteristicMapper.toCharacteristicDocument(characteristic));
    }

    public void createAll(List<CharacteristicDTO> characteristics) {
        characteristicRedisRepo.saveAll(
                characteristicMapper.toCharacteristicDocument(characteristics));
    }

    public void updateOne(CharacteristicDTO characteristic) {
        characteristicRedisRepo.save(
                characteristicMapper.toCharacteristicDocument(characteristic));
    }

    public void updateAll(List<CharacteristicDTO> characteristics) {
        characteristicRedisRepo.saveAll(
                characteristicMapper.toCharacteristicDocument(characteristics));
    }

    public void deleteOne(Long id) {
        characteristicRedisRepo.deleteById(id);
    }

    public void deleteAll(List<Long> ids) {
        characteristicRedisRepo.deleteAllById(ids);
    }
}
