package com.ak.store.synchronization.facade;

import com.ak.store.common.model.catalogue.dto.CharacteristicDTO;
import com.ak.store.synchronization.service.CharacteristicRedisService;
import com.ak.store.synchronization.util.mapper.CharacteristicMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacteristicFacade {
    private final CharacteristicMapper characteristicMapper;
    private final CharacteristicRedisService characteristicRedisService;

    public void createOne(CharacteristicDTO characteristic) {
        characteristicRedisService.createOne(characteristicMapper.toCharacteristicDocument(characteristic));
    }

    public void createAll(List<CharacteristicDTO> characteristics) {
        characteristicRedisService.createAll(characteristicMapper.toCharacteristicDocument(characteristics));
    }

    public void updateOne(CharacteristicDTO characteristic) {
        characteristicRedisService.updateOne(characteristicMapper.toCharacteristicDocument(characteristic));
    }

    public void updateAll(List<CharacteristicDTO> characteristics) {
        characteristicRedisService.updateAll(characteristicMapper.toCharacteristicDocument(characteristics));
    }

    public void deleteOne(Long id) {
        characteristicRedisService.deleteOne(id);
    }

    public void deleteAll(List<Long> ids) {
        characteristicRedisService.deleteAll(ids);
    }
}
