package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.facade.CategoryServiceFacade;
import com.ak.store.common.model.catalogue.dto.CategoryDTO;
import com.ak.store.common.model.catalogue.view.CategoryView;
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
    public List<CategoryView> getAll() {
        return categoryServiceFacade.findAll();
    }

    @PostMapping
    public Long createOne(@RequestBody @Valid CategoryDTO categoryDTO) {
        return categoryServiceFacade.createOne(categoryDTO);
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
    public Long updateOne(@PathVariable Long id, @RequestBody @Valid CategoryDTO categoryDTO) {
        return categoryServiceFacade.updateOne(id, categoryDTO);
    }
}
