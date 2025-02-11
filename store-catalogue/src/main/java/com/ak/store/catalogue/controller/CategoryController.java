package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.facade.CategoryServiceFacade;
import com.ak.store.common.dto.catalogue.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue/categories")
public class CategoryController {
    private final CategoryServiceFacade categoryServiceFacade;

    @GetMapping
    public List<CategoryDTO> getAllCategory() {
        return categoryServiceFacade.findAllCategory();
    }
}
