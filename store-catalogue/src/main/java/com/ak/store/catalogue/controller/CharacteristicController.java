package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.facade.CharacteristicServiceFacade;
import com.ak.store.common.model.catalogue.dto.CharacteristicDTO;
import com.ak.store.common.model.catalogue.dto.RangeValueDTO;
import com.ak.store.common.model.catalogue.dto.TextValueDTO;
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
    public void createOne(@RequestBody @Valid CharacteristicDTO characteristicDTO) {
        characteristicServiceFacade.createOne(characteristicDTO);
    }

    @DeleteMapping("{id}")
    public void deleteOne(@PathVariable Long id) {
        characteristicServiceFacade.deleteOne(id);
    }

    @PatchMapping("{id}")
    public void updateOne(@PathVariable Long id, @RequestBody CharacteristicDTO characteristicDTO) {
        characteristicServiceFacade.updateOne(id, characteristicDTO);
    }

    @PostMapping("{id}/range")
    public void createOneRangeValue(@PathVariable Long id, @RequestBody @Valid RangeValueDTO rangeValueDTO) {
        characteristicServiceFacade.createOneRangeValue(id, rangeValueDTO);
    }

    @PostMapping("{id}/text")
    public void createOneTextValue(@PathVariable Long id, @RequestBody @Valid TextValueDTO textValueDTO) {
        characteristicServiceFacade.createOneTextValue(id, textValueDTO);
    }

    @DeleteMapping("{id}/range")
    public void deleteOneRangeValue(@PathVariable Long id, @RequestBody @Valid RangeValueDTO rangeValueDTO) {
        characteristicServiceFacade.deleteOneRangeValue(id, rangeValueDTO);
    }

    @DeleteMapping("{id}/text")
    public void deleteOneRangeValue(@PathVariable Long id, @RequestBody @Valid TextValueDTO textValueDTO) {
        characteristicServiceFacade.deleteOneTextValue(id, textValueDTO);
    }
}