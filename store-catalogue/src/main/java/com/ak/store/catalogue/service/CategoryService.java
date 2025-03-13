package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.CategoryCharacteristic;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.repository.CategoryRepo;
import com.ak.store.catalogue.validator.business.CategoryBusinessValidator;
import com.ak.store.catalogue.util.mapper.CatalogueMapper0;
import com.ak.store.common.model.catalogue.form.CategoryForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CatalogueMapper0 catalogueMapper0;
    private final CategoryRepo categoryRepo;
    private final CharacteristicService characteristicService;
    private final CategoryBusinessValidator categoryBusinessValidator;

    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    public Category createOne(CategoryForm categoryForm) {
        categoryBusinessValidator.validateCreation(categoryForm);
        return categoryRepo.save(catalogueMapper0.mapToCategory(categoryForm));
    }

    private Category findOneWithCharacteristics(Long id) {
        return categoryRepo.findOneWithCharacteristicsById(id)
                .orElseThrow(() -> new RuntimeException("no category found"));
    }

    public Category findOne(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("no category found"));
    }

    public Category addCharacteristicToCategory(Long categoryId, Long characteristicId) {
        Category category = findOneWithCharacteristics(categoryId);
        //check for characteristic exists
        characteristicService.findOne(characteristicId);
        categoryBusinessValidator.validateAddCharacteristic(category, characteristicId);

        category.getCharacteristics().add(
                CategoryCharacteristic.builder()
                        .category(Category.builder().id(categoryId).build())
                        .characteristic(Characteristic.builder().id(characteristicId).build())
                        .build()
        );
        return categoryRepo.save(category);
    }

    public Category deleteCharacteristicFromCategory(Long categoryId, Long characteristicId) {
        Category category = findOneWithCharacteristics(categoryId);
        categoryBusinessValidator.validateDeleteCharacteristic(category, characteristicId);

        for (int i = 0; i < category.getCharacteristics().size(); i++) {
            if(category.getCharacteristics().get(i).getCharacteristic().getId().equals(characteristicId)) {
                category.getCharacteristics().remove(i);
                break;
            }
        }

        return categoryRepo.save(category);
    }

    //todo: make check if product has this category
    public void deleteOne(Long id) {
        Category category = findOne(id);
        categoryBusinessValidator.validateDeletion(category);
        categoryRepo.delete(category);
    }

    public Category updateOne(Long id, CategoryForm categoryForm) {
        Category category = findOne(id);
        categoryBusinessValidator.validateUpdate(category, categoryForm);
        updateCategory(category, categoryForm);
        return categoryRepo.save(category);
    }

    private void updateCategory(Category category, CategoryForm categoryForm) {
        if(categoryForm.getName() != null) {
            category.setName(categoryForm.getName());
        }
        if(categoryForm.getParentId() != null) {
            category.setParentId(categoryForm.getParentId());
        }
    }
}
