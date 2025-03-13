package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.facade.CharacteristicServiceFacade;
import com.ak.store.common.model.catalogue.form.CharacteristicForm;
import com.ak.store.common.model.catalogue.form.RangeValueForm;
import com.ak.store.common.model.catalogue.form.TextValueForm;
import com.ak.store.common.model.catalogue.view.CharacteristicView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue/characteristics")
public class CharacteristicController {
    private final CharacteristicServiceFacade characteristicServiceFacade;

    @GetMapping
    public List<CharacteristicView> getAllByCategory(@RequestParam Long categoryId) {
        return characteristicServiceFacade.findAllByCategoryId(categoryId);
    }

    @PostMapping
    public Long createOne(@RequestBody @Valid CharacteristicForm characteristicForm) {
        return characteristicServiceFacade.createOne(characteristicForm);
    }

    @DeleteMapping("{id}")
    public void deleteOne(@PathVariable Long id) {
        characteristicServiceFacade.deleteOne(id);
    }

    @PatchMapping("{id}")
    public Long updateOne(@PathVariable Long id, @RequestBody CharacteristicForm characteristicForm) {
        return characteristicServiceFacade.updateOne(id, characteristicForm);
    }

    @PostMapping("{id}/range")
    public Long createOneRangeValue(@PathVariable Long id, @RequestBody @Valid RangeValueForm rangeValueForm) {
        return characteristicServiceFacade.createOneRangeValue(id, rangeValueForm);
    }

    @PostMapping("{id}/text")
    public Long createOneTextValue(@PathVariable Long id, @RequestBody @Valid TextValueForm textValueForm) {
        return characteristicServiceFacade.createOneTextValue(id, textValueForm);
    }

    @DeleteMapping("{id}/range")
    public Long deleteOneRangeValue(@PathVariable Long id, @RequestBody @Valid RangeValueForm rangeValueForm) {
        return characteristicServiceFacade.deleteOneRangeValue(id, rangeValueForm);
    }

    @DeleteMapping("{id}/text")
    public Long deleteOneRangeValue(@PathVariable Long id, @RequestBody @Valid TextValueForm textValueForm) {
        return characteristicServiceFacade.deleteOneTextValue(id, textValueForm);
    }
}