package com.ak.store.catalogue.category.controller;

import com.ak.store.catalogue.category.facade.CategoryFacade;
import com.ak.store.catalogue.category.mapper.CategoryMapper;
import com.ak.store.catalogue.model.form.WriteCategoryCharacteristicForm;
import com.ak.store.catalogue.model.form.WriteCategoryForm;
import com.ak.store.catalogue.model.validationGroup.Create;
import com.ak.store.catalogue.model.validationGroup.Update;
import com.ak.store.catalogue.model.view.CategoryTreeView;
import com.ak.store.catalogue.util.CategoryTreeBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue/categories")
public class CategoryController {
    private final CategoryFacade categoryFacade;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public List<CategoryTreeView> findAll() {
        var categories = categoryFacade.findAll();
        return CategoryTreeBuilder.build(categories.stream()
                .map(categoryMapper::toTreeView)
                .toList());
    }

    @PostMapping
    public Long createOne(@RequestBody @Validated(Create.class) WriteCategoryForm form) {
        return categoryFacade.createOne(categoryMapper.toWriteCommand(form));
    }

    @DeleteMapping("{id}")
    public Long deleteOne(@PathVariable Long id) {
        return categoryFacade.deleteOne(id);
    }

    @PatchMapping
    public Long updateOne(@RequestBody @Validated(Update.class) WriteCategoryForm form) {
        return categoryFacade.updateOne(categoryMapper.toWriteCommand(form));
    }

    @PostMapping("{categoryId}/characteristics/{characteristicId}")
    public Long addOneCharacteristic(@PathVariable Long categoryId,
                                  @PathVariable Long characteristicId) {
        var form = WriteCategoryCharacteristicForm.builder()
                .categoryId(categoryId)
                .characteristicId(characteristicId)
                .build();
        return categoryFacade.addOneCharacteristic(categoryMapper.toWriteCharacteristicCommand(form));
    }

    @DeleteMapping("{categoryId}/characteristics/{characteristicId}")
    public Long removeOneCharacteristic(@PathVariable Long categoryId,
                                     @PathVariable Long characteristicId) {
        var form = WriteCategoryCharacteristicForm.builder()
                .categoryId(categoryId)
                .characteristicId(characteristicId)
                .build();
        return categoryFacade.removeOneCharacteristic(categoryMapper.toWriteCharacteristicCommand(form));
    }
}
