package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.facade.CharacteristicFacade;
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
    private final CharacteristicFacade characteristicFacade;

    @GetMapping
    public List<CharacteristicView> getAllByCategory(@RequestParam Long categoryId) {
        return characteristicFacade.findAllByCategoryId(categoryId);
    }

    @PostMapping
    public Long createOne(@RequestBody @Valid CharacteristicForm characteristicForm) {
        return characteristicFacade.createOne(characteristicForm);
    }

    @DeleteMapping("{id}")
    public void deleteOne(@PathVariable Long id) {
        characteristicFacade.deleteOne(id);
    }

    @PatchMapping("{id}")
    public Long updateOne(@PathVariable Long id, @RequestBody CharacteristicForm characteristicForm) {
        return characteristicFacade.updateOne(id, characteristicForm);
    }

    @PostMapping("{id}/range")
    public Long createOneRangeValue(@PathVariable Long id, @RequestBody @Valid RangeValueForm rangeValueForm) {
        return characteristicFacade.createOneRangeValue(id, rangeValueForm);
    }

    @PostMapping("{id}/text")
    public Long createOneTextValue(@PathVariable Long id, @RequestBody @Valid TextValueForm textValueForm) {
        return characteristicFacade.createOneTextValue(id, textValueForm);
    }

    @DeleteMapping("{id}/range")
    public Long deleteOneRangeValue(@PathVariable Long id, @RequestBody @Valid RangeValueForm rangeValueForm) {
        return characteristicFacade.deleteOneRangeValue(id, rangeValueForm);
    }

    @DeleteMapping("{id}/text")
    public Long deleteOneRangeValue(@PathVariable Long id, @RequestBody @Valid TextValueForm textValueForm) {
        return characteristicFacade.deleteOneTextValue(id, textValueForm);
    }
}