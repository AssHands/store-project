package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.service.CharacteristicService;
import com.ak.store.common.dto.catalogue.CharacteristicDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacteristicServiceFacade {
    private final CharacteristicService characteristicService;

    public List<CharacteristicDTO> findAllCharacteristicByCategoryId(Long categoryId) {
        return characteristicService.findAllCharacteristicByCategoryId(categoryId);
    }
}
