package com.ak.store.catalogue.service;

import com.ak.store.catalogue.repository.CategoryRepo;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.catalogue.util.CatalogueUtils;
import com.ak.store.common.dto.catalogue.product.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CatalogueMapper catalogueMapper;
    private final CategoryRepo categoryRepo;

    public List<CategoryDTO> findAllCategory() {
        return categoryRepo.findAll().stream()
                .map(catalogueMapper::mapToCategoryDTO)
                .toList();
    }
}
