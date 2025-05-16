package com.ak.store.synchronization.facade;

import com.ak.store.synchronization.service.CharacteristicRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacteristicFacade {
    private final CharacteristicRedisService characteristicRedisService;

    public void createAll(List<CharacteristicDTOold> characteristics) {
        characteristicRedisService.createAll(characteristics);
    }

    public void updateAll(List<CharacteristicDTOold> characteristics) {
        characteristicRedisService.updateAll(characteristics);
    }

    public void deleteAll(List<Long> ids) {
        characteristicRedisService.deleteAll(ids);
    }
}
