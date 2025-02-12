package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.service.CharacteristicService;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.common.model.catalogue.view.CharacteristicView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacteristicServiceFacade {
    private final CharacteristicService characteristicService;

    private final CatalogueMapper catalogueMapper;

    public List<CharacteristicView> findAllCharacteristicByCategoryId(Long categoryId) {
        return characteristicService.findAllCharacteristicByCategoryId(categoryId).stream()
                .map(catalogueMapper::mapToCharacteristicView)
                .toList();
    }
}
