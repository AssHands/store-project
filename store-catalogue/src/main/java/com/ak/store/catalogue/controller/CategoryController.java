package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.facade.CategoryFacade;
import com.ak.store.common.model.catalogue.form.CategoryForm;
import com.ak.store.common.model.catalogue.view.CategoryTreeView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue/categories")
public class CategoryController {
    private final CategoryFacade categoryFacade;

    @GetMapping
    public List<CategoryTreeView> getAll() {
        return categoryFacade.findAllAsTree();
    }

    @PostMapping
    public Long createOne(@RequestBody @Valid CategoryForm categoryForm) {
        return categoryFacade.createOne(categoryForm);
    }

    @DeleteMapping("{id}")
    public void deleteOne(@PathVariable Long id) {
        categoryFacade.deleteOne(id);
    }

    @PostMapping("{id}/characteristics")
    public Long addCharacteristicToCategory(@PathVariable("id") Long categoryId,
                                            @RequestParam("characteristic") Long characteristicId) {
        return categoryFacade.addCharacteristicToCategory(categoryId, characteristicId);
    }

    @DeleteMapping("{id}/characteristics")
    public Long deleteCharacteristicFromCategory(@PathVariable("id") Long categoryId,
                                                 @RequestParam("characteristic") Long characteristicId) {
        return categoryFacade.deleteCharacteristicFromCategory(categoryId, characteristicId);
    }

    @PatchMapping("{id}")
    public Long updateOne(@PathVariable Long id, @RequestBody @Valid CategoryForm categoryForm) {
        return categoryFacade.updateOne(id, categoryForm);
    }
}
