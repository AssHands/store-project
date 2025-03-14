package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.facade.CategoryServiceFacade;
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
    private final CategoryServiceFacade categoryServiceFacade;

    @GetMapping
    public List<CategoryTreeView> getAll() {
        return categoryServiceFacade.findAllAsTree();
    }

    @PostMapping
    public Long createOne(@RequestBody @Valid CategoryForm categoryForm) {
        return categoryServiceFacade.createOne(categoryForm);
    }

    @DeleteMapping("{id}")
    public void deleteOne(@PathVariable Long id) {
        categoryServiceFacade.deleteOne(id);
    }

    @PostMapping("{id}/characteristics")
    public Long addCharacteristicToCategory(@PathVariable("id") Long categoryId,
                                            @RequestParam("characteristic") Long characteristicId) {
        return categoryServiceFacade.addCharacteristicToCategory(categoryId, characteristicId);
    }

    @DeleteMapping("{id}/characteristics")
    public Long deleteCharacteristicFromCategory(@PathVariable("id") Long categoryId,
                                                 @RequestParam("characteristic") Long characteristicId) {
        return categoryServiceFacade.deleteCharacteristicFromCategory(categoryId, characteristicId);
    }

    @PatchMapping("{id}")
    public Long updateOne(@PathVariable Long id, @RequestBody @Valid CategoryForm categoryForm) {
        return categoryServiceFacade.updateOne(id, categoryForm);
    }
}
