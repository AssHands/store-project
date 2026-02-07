package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.facade.CategoryFacade;
import com.ak.store.catalogue.mapper.CategoryMapper;
import com.ak.store.catalogue.model.form.WriteCategoryCharacteristicForm;
import com.ak.store.catalogue.model.form.WriteCategoryForm;
import com.ak.store.catalogue.model.validationGroup.Create;
import com.ak.store.catalogue.model.validationGroup.Update;
import com.ak.store.catalogue.model.view.CategoryTreeView;
import com.ak.store.catalogue.util.CategoryTreeBuilder;
import jakarta.validation.Valid;
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

    @PatchMapping("update")
    public Long updateOne(@RequestBody @Validated(Update.class) WriteCategoryForm form) {
        return categoryFacade.updateOne(categoryMapper.toWriteCommand(form));
    }

    @PostMapping("update/characteristics/add")
    public Long addOneCharacteristic(@RequestBody @Valid WriteCategoryCharacteristicForm form) {
        return categoryFacade.addOneCharacteristic(categoryMapper.toWriteCharacteristicCommand(form));
    }

    @PostMapping("update/characteristics/remove")
    public Long removeOneCharacteristic(@RequestBody @Valid WriteCategoryCharacteristicForm form) {
        return categoryFacade.removeOneCharacteristic(categoryMapper.toWriteCharacteristicCommand(form));
    }
}
