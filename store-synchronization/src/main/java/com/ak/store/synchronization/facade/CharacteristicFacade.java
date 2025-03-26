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
    private final CharacteristicRedisService characteristicRedisService;

    public void createAll(List<CharacteristicDTO> characteristics) {
        characteristicRedisService.createAll(characteristics);
    }

    public void updateAll(List<CharacteristicDTO> characteristics) {
        characteristicRedisService.updateAll(characteristics);
    }

    public void deleteAll(List<Long> ids) {
        characteristicRedisService.deleteAll(ids);
    }
}
