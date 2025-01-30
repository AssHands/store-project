package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.service.CatalogueService;
import com.ak.store.common.dto.catalogue.product.CategoryDTO;
import com.ak.store.common.dto.search.Filters;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue")
public class CatalogueController {
    private final CatalogueService catalogueService;

    @GetMapping("categories")
    public List<CategoryDTO> getAllCategory() {
        return catalogueService.findAllCategory();
    }

    @GetMapping("characteristics")
    public Filters getAllAvailableCharacteristicByCategory(@RequestParam Long categoryId) {
        return catalogueService.findAllAvailableCharacteristicByCategory(categoryId);
    }
}
