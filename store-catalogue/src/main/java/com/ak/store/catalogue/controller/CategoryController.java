package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.facade.CategoryFacade;
import com.ak.store.catalogue.model.form.CategoryForm;
import com.ak.store.catalogue.model.view.CategoryTreeView;
import com.ak.store.catalogue.util.CategoryTreeBuilder;
import com.ak.store.catalogue.mapper.CategoryMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
                .map(categoryMapper::toCategoryTreeView)
                .toList());
    }

    @PostMapping
    public Long createOne(@RequestBody @Valid CategoryForm request) {
        return categoryFacade.createOne(categoryMapper.toCategoryWriteDTO(request));
    }

    @DeleteMapping("{id}")
    public void deleteOne(@PathVariable Long id) {
        categoryFacade.deleteOne(id);
    }

    @PatchMapping("{id}")
    public Long updateOne(@PathVariable Long id, @RequestBody @Valid CategoryForm request) {
        return categoryFacade.updateOne(id, categoryMapper.toCategoryWriteDTO(request));
    }

    @PostMapping("{id}/characteristics/{characteristicId}")
    public Long addOneCharacteristic(@PathVariable("id") Long categoryId,
                                     @PathVariable("characteristicId") Long characteristicId) {
        return categoryFacade.addOneCharacteristic(categoryId, characteristicId);
    }

    @DeleteMapping("{id}/characteristics/{characteristicId}")
    public Long removeOneCharacteristic(@PathVariable("id") Long categoryId,
                                        @PathVariable("characteristicId") Long characteristicId) {
        return categoryFacade.removeOneCharacteristic(categoryId, characteristicId);
    }

    @PostMapping("{id}/related/{relatedId}")
    public Long addOneRelatedCategory(@PathVariable("id") Long categoryId,
                                      @PathVariable("relatedId") Long relatedId) {
        return categoryFacade.addOneRelatedCategory(categoryId, relatedId);
    }

    @DeleteMapping("{id}/related/{relatedId}")
    public Long removeOneRelatedCategory(@PathVariable("id") Long categoryId,
                                         @PathVariable("relatedId") Long relatedId) {
        return categoryFacade.removeOneRelatedFromCategory(categoryId, relatedId);
    }
}
