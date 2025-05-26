package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.facade.CharacteristicFacade;
import com.ak.store.catalogue.model.form.CharacteristicForm;
import com.ak.store.catalogue.model.form.NumericValueForm;
import com.ak.store.catalogue.model.form.TextValueForm;
import com.ak.store.catalogue.model.validationGroup.Update;
import com.ak.store.catalogue.model.view.CharacteristicView;
import com.ak.store.catalogue.mapper.CharacteristicMapper;
import com.ak.store.catalogue.model.validationGroup.Create;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue/characteristics")
public class CharacteristicController {
    private final CharacteristicFacade characteristicFacade;
    private final CharacteristicMapper characteristicMapper;

    @GetMapping
    public List<CharacteristicView> findAllByCategoryId(@RequestParam Long categoryId) {
        return characteristicMapper.toCharacteristicView(
                characteristicFacade.findAllByCategoryId(categoryId)
        );
    }

    @PostMapping
    public Long createOne(@RequestBody @Validated(Create.class) CharacteristicForm request) {
        return characteristicFacade.createOne(characteristicMapper.toCharacteristicWriteDTO(request));
    }

    @PatchMapping("{id}")
    public Long updateOne(@PathVariable Long id,
                          @RequestBody @Validated(Update.class) CharacteristicForm request) {
        return characteristicFacade.updateOne(id, characteristicMapper.toCharacteristicWriteDTO(request));
    }

    @DeleteMapping("{id}")
    public void deleteOne(@PathVariable Long id) {
        characteristicFacade.deleteOne(id);
    }

    @PostMapping("{id}/numeric")
    public Long addOneNumericValue(@PathVariable Long id, @RequestBody @Valid NumericValueForm request) {
        return characteristicFacade.addOneNumericValue(id, characteristicMapper.toNumericValueWriteDTO(request));
    }

    @DeleteMapping("{id}/numeric")
    public Long removeOneNumericValue(@PathVariable Long id, @RequestBody @Valid NumericValueForm request) {
        return characteristicFacade.removeOneNumericValue(id, characteristicMapper.toNumericValueWriteDTO(request));
    }

    @PostMapping("{id}/text")
    public Long addOneTextValue(@PathVariable Long id, @RequestBody @Valid TextValueForm request) {
        return characteristicFacade.addOneTextValue(id, characteristicMapper.toTextValueWriteDTO(request));
    }

    @DeleteMapping("{id}/text")
    public Long removeOneTextValue(@PathVariable Long id, @RequestBody @Valid TextValueForm request) {
        return characteristicFacade.removeOneTextValue(id, characteristicMapper.toTextValueWriteDTO(request));
    }
}