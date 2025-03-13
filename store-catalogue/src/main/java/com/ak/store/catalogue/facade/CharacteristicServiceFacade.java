package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.service.CharacteristicService;
import com.ak.store.catalogue.util.CatalogueMapper0;
import com.ak.store.common.model.catalogue.form.CharacteristicForm;
import com.ak.store.common.model.catalogue.form.RangeValueForm;
import com.ak.store.common.model.catalogue.form.TextValueForm;
import com.ak.store.common.model.catalogue.view.CharacteristicView;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CharacteristicServiceFacade {
    private final CharacteristicService characteristicService;

    private final CatalogueMapper0 catalogueMapper0;

    public List<CharacteristicView> findAllByCategoryId(Long categoryId) {
        return characteristicService.findAllByCategoryId(categoryId).stream()
                .map(catalogueMapper0::mapToCharacteristicView)
                .toList();
    }

    @Transactional
    public Long createOne(CharacteristicForm characteristicForm) {
        return characteristicService.createOne(characteristicForm).getId();
    }

    @Transactional
    public void deleteOne(Long id) {
        characteristicService.deleteOne(id);
    }

    @Transactional
    public Long updateOne(Long id, CharacteristicForm characteristicForm) {
        return characteristicService.updateOne(id, characteristicForm).getId();
    }

    @Transactional
    public Long createOneRangeValue(Long id, RangeValueForm rangeValueForm) {
        return characteristicService.createRangeValue(id, rangeValueForm).getId();
    }

    @Transactional
    public Long createOneTextValue(Long id, TextValueForm textValueForm) {
        return characteristicService.createTextValue(id, textValueForm).getId();
    }

    @Transactional
    public Long deleteOneRangeValue(Long id, RangeValueForm rangeValueForm) {
        return characteristicService.deleteOneRangeValue(id, rangeValueForm).getId();
    }

    @Transactional
    public Long deleteOneTextValue(Long id, TextValueForm textValueForm) {
        return characteristicService.deleteOneTextValue(id, textValueForm).getId();
    }
}
