package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.service.CharacteristicService;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.common.model.catalogue.dto.CharacteristicDTO;
import com.ak.store.common.model.catalogue.dto.RangeValueDTO;
import com.ak.store.common.model.catalogue.dto.TextValueDTO;
import com.ak.store.common.model.catalogue.view.CharacteristicView;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacteristicServiceFacade {
    private final CharacteristicService characteristicService;

    private final CatalogueMapper catalogueMapper;

    public List<CharacteristicView> findAllByCategoryId(Long categoryId) {
        return characteristicService.findAllByCategoryId(categoryId).stream()
                .map(catalogueMapper::mapToCharacteristicView)
                .toList();
    }

    @Transactional
    public Long createOne(CharacteristicDTO characteristicDTO) {
        return characteristicService.createOne(characteristicDTO).getId();
    }

    @Transactional
    public void deleteOne(Long id) {
        characteristicService.deleteOne(id);
    }

    @Transactional
    public Long updateOne(Long id, CharacteristicDTO characteristicDTO) {
        return characteristicService.updateOne(id, characteristicDTO).getId();
    }

    @Transactional
    public Long createOneRangeValue(Long id, RangeValueDTO rangeValueDTO) {
        return characteristicService.createRangeValue(id, rangeValueDTO).getId();
    }

    @Transactional
    public Long createOneTextValue(Long id, TextValueDTO textValueDTO) {
        return characteristicService.createTextValue(id, textValueDTO).getId();
    }

    @Transactional
    public Long deleteOneRangeValue(Long id, RangeValueDTO rangeValueDTO) {
        return characteristicService.deleteOneRangeValue(id, rangeValueDTO).getId();
    }

    @Transactional
    public Long deleteOneTextValue(Long id,TextValueDTO textValueDTO) {
        return characteristicService.deleteOneTextValue(id, textValueDTO).getId();
    }
}
