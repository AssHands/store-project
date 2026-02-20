package com.ak.store.catalogue.characteristic.controller;

import com.ak.store.catalogue.characteristic.facade.CharacteristicFacade;
import com.ak.store.catalogue.characteristic.mapper.CharacteristicMapper;
import com.ak.store.catalogue.model.form.WriteCharacteristicForm;
import com.ak.store.catalogue.model.form.WriteNumericValueForm;
import com.ak.store.catalogue.model.form.WriteTextValueForm;
import com.ak.store.catalogue.model.validationGroup.Update;
import com.ak.store.catalogue.model.view.CharacteristicView;
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
        return characteristicFacade.findAllByCategoryId(categoryId).stream()
                .map(characteristicMapper::toView)
                .toList();
    }

    @PostMapping
    public Long createOne(@RequestBody @Validated(Create.class) WriteCharacteristicForm form) {
        return characteristicFacade.createOne(characteristicMapper.toWriteCommand(form));
    }

    @PatchMapping("update")
    public Long updateOne(@RequestBody @Validated(Update.class) WriteCharacteristicForm form) {
        return characteristicFacade.updateOne(characteristicMapper.toWriteCommand(form));
    }

    @DeleteMapping("{id}")
    public void deleteOne(@PathVariable Long id) {
        characteristicFacade.deleteOne(id);
    }

    @PostMapping("update/numeric/add")
    public Long addOneNumericValue(@RequestBody @Valid WriteNumericValueForm form) {
        return characteristicFacade.addOneNumericValue(characteristicMapper.toWriteNumericValueCommand(form));
    }

    @DeleteMapping("update/numeric/remove")
    public Long removeOneNumericValue(@RequestBody @Valid WriteNumericValueForm form) {
        return characteristicFacade.removeOneNumericValue(characteristicMapper.toWriteNumericValueCommand(form));
    }

    @PostMapping("update/text/add")
    public Long addOneTextValue(@RequestBody @Valid WriteTextValueForm form) {
        return characteristicFacade.addOneTextValue(characteristicMapper.toWriteTextValueCommand(form));
    }

    @PostMapping("update/text/remove")
    public Long removeOneTextValue(@RequestBody @Valid WriteTextValueForm form) {
        return characteristicFacade.removeOneTextValue(characteristicMapper.toWriteTextValueCommand(form));
    }
}