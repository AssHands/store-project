package com.ak.store.catalogue.category.service.query;

import com.ak.store.catalogue.exception.NotFoundException;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryQueryService {
    private final CategoryRepo categoryRepo;

    public boolean existsByName(String name) {
        return categoryRepo.existsByNameEqualsIgnoreCase(name);
    }

    public boolean existsChildren(Long id) {
        return categoryRepo.existsByParentId(id);
    }

    public Category findByIdOrThrow(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: id=" + id));
    }

    public Category findOneFullByIdOrThrow(Long id) {
        return categoryRepo.findOneFullById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: id=" + id));
    }
}
