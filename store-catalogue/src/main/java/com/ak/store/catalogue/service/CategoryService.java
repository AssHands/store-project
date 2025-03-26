package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.CategoryCharacteristic;
import com.ak.store.catalogue.repository.CategoryRepo;
import com.ak.store.catalogue.util.mapper.CategoryMapper;
import com.ak.store.catalogue.validator.service.CategoryServiceValidator;
import com.ak.store.common.model.catalogue.form.CategoryForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepo categoryRepo;
    private final CharacteristicService characteristicService;
    private final CategoryServiceValidator categoryServiceValidator;

    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    public Category findOne(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("no category found"));
    }

    public Category findOneWithCharacteristics(Long id) {
        return categoryRepo.findOneWithCharacteristicsById(id)
                .orElseThrow(() -> new RuntimeException("no category found"));
    }

    public Category findOneWithAll(Long id) {
        Category category = categoryRepo.findOneWithCharacteristicsById(id)
                .orElseThrow(() -> new RuntimeException("no category found"));

        category.getRelatedCategories().size();
        return category;
    }

    public Category createOne(CategoryForm categoryForm) {
        categoryServiceValidator.validateCreate(categoryForm);
        return categoryRepo.save(categoryMapper.toCategory(categoryForm));
    }

    public Category addCharacteristicToCategory(Long categoryId, Long characteristicId) {
        var category = findOneWithCharacteristics(categoryId);
        categoryServiceValidator.validateAddCharacteristic(category, characteristicId);
        var characteristic = characteristicService.findOne(characteristicId);

        category.getCharacteristics().add(
                CategoryCharacteristic.builder()
                        .category(category)
                        .characteristic(characteristic)
                        .build()
        );
        return categoryRepo.save(category);
    }

    public Category deleteCharacteristicFromCategory(Long categoryId, Long characteristicId) {
        Category category = findOneWithCharacteristics(categoryId);
        categoryServiceValidator.validateDeleteCharacteristic(category, characteristicId);

        for (int i = 0; i < category.getCharacteristics().size(); i++) {
            if (category.getCharacteristics().get(i).getCharacteristic().getId().equals(characteristicId)) {
                category.getCharacteristics().remove(i);
                break;
            }
        }

        return categoryRepo.save(category);
    }

    //todo: make check if product has this category
    public Category deleteOne(Long id) {
        Category category = findOne(id);
        categoryServiceValidator.validateDelete(category);
        categoryRepo.delete(category);
        return category;
    }

    public Category updateOne(Long id, CategoryForm categoryForm) {
        Category category = findOne(id);
        categoryServiceValidator.validateUpdate(category, categoryForm);
        updateCategory(category, categoryForm);
        return categoryRepo.save(category);
    }

    private void updateCategory(Category category, CategoryForm categoryForm) {
        if (categoryForm.getName() != null) {
            category.setName(categoryForm.getName());
        }
        if (categoryForm.getParentId() != null) {
            category.setParentId(categoryForm.getParentId());
        }
    }

    public Category addRelatedToCategory(Long categoryId, Long relatedId) {
        Category category = findOneWithAll(categoryId);
        categoryServiceValidator.validateAddRelated(category, relatedId);

        category.getRelatedCategories().add(Category.builder().id(relatedId).build());
        return categoryRepo.save(category);
    }

    public Category deleteRelatedFromCategory(Long categoryId, Long relatedId) {
        Category category = findOneWithAll(categoryId);
        categoryServiceValidator.validateDeleteRelated(category, relatedId);

        category.getRelatedCategories().remove(Category.builder().id(relatedId).build());
        return categoryRepo.save(category);
    }
}
