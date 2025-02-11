package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.service.CategoryService;
import com.ak.store.catalogue.util.CatalogueUtils;
import com.ak.store.common.dto.catalogue.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceFacade {
    private final CategoryService categoryService;

    public List<CategoryDTO> findAllCategory() {
        return CatalogueUtils.buildCategoryTree(categoryService.findAllCategory());
    }
}
