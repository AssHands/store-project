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
    public List<CategoryView> getAllCategory() {
        return categoryServiceFacade.findAll();
    }

    @PostMapping
    public void createOneCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        categoryServiceFacade.createOne(categoryDTO);
    }

    @DeleteMapping("{id}")
    public void deleteOneCategory(@PathVariable Long id) {
        categoryServiceFacade.deleteOne(id);
    }

    @PostMapping("{id}/characteristics")
    public void addCharacteristicToCategory(@PathVariable("id") Long categoryId,
                                            @RequestParam("characteristic") Long characteristicId) {
        categoryServiceFacade.addCharacteristicToCategory(categoryId, characteristicId);
    }

    @DeleteMapping("{id}/characteristics")
    public void deleteCharacteristicToCategory(@PathVariable("id") Long categoryId,
                                            @RequestParam("characteristic") Long characteristicId) {
        categoryServiceFacade.deleteCharacteristicToCategory(categoryId, characteristicId);
    }

    @PatchMapping("{id}")
    public void updateOneCategory(@PathVariable Long id, @RequestBody @Valid CategoryDTO categoryDTO) {
        categoryServiceFacade.updateOne(id, categoryDTO);
    }
}
