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
    public void createOne(CharacteristicDTO characteristicDTO) {
        characteristicService.createOne(characteristicDTO);
    }

    @Transactional
    public void deleteOne(Long id) {
        characteristicService.deleteOne(id);
    }

    @Transactional
    public void updateOne(Long id, CharacteristicDTO characteristicDTO) {
        characteristicService.updateOne(id, characteristicDTO);
    }

    @Transactional
    public void createOneRangeValue(Long id, RangeValueDTO rangeValueDTO) {
        characteristicService.createRangeValue(id, rangeValueDTO);
    }

    @Transactional
    public void createOneTextValue(Long id, TextValueDTO textValueDTO) {
        characteristicService.createTextValue(id, textValueDTO);
    }

    @Transactional
    public void deleteOneRangeValue(Long id, RangeValueDTO rangeValueDTO) {
        characteristicService.deleteOneRangeValue(id, rangeValueDTO);
    }

    @Transactional
    public void deleteOneTextValue(Long id,TextValueDTO textValueDTO) {
        characteristicService.deleteOneTextValue(id, textValueDTO);
    }
}
