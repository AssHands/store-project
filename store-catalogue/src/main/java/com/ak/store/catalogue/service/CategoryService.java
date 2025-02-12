package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.CategoryCharacteristic;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.repository.CategoryRepo;
import com.ak.store.catalogue.validator.CategoryBusinessValidator;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.common.model.catalogue.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CatalogueMapper catalogueMapper;
    private final CategoryRepo categoryRepo;
    private final CharacteristicService characteristicService;
    private final CategoryBusinessValidator categoryBusinessValidator;

    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    public void createOne(CategoryDTO categoryDTO) {
        categoryBusinessValidator.validateCreation(categoryDTO);
        categoryRepo.save(catalogueMapper.mapToCategory(categoryDTO));
    }

    private Category findOneWithCharacteristics(Long id) {
        return categoryRepo.findOneWithCharacteristicsById(id)
                .orElseThrow(() -> new RuntimeException("no category found"));
    }

    public Category findOne(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("no category found"));
    }

    public void addCharacteristicToCategory(Long categoryId, Long characteristicId) {
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
        categoryRepo.save(category);
    }

    public void deleteCharacteristicFromCategory(Long categoryId, Long characteristicId) {
        Category category = findOneWithCharacteristics(categoryId);
        categoryBusinessValidator.validateDeleteCharacteristic(category, characteristicId);

        for (int i = 0; i < category.getCharacteristics().size(); i++) {
            if(category.getCharacteristics().get(i).getCharacteristic().getId().equals(characteristicId)) {
                category.getCharacteristics().remove(i);
                break;
            }
        }

        categoryRepo.save(category);
    }

    //todo: make check if product has this category
    public void deleteOne(Long id) {
        Category category = findOne(id);
        categoryBusinessValidator.validateDeletion(category);
        categoryRepo.delete(category);
    }

    public void updateOne(Long id, CategoryDTO categoryDTO) {
        Category category = findOne(id);
        categoryBusinessValidator.validateUpdate(category, categoryDTO);
        updateCategory(category, categoryDTO);
        categoryRepo.save(category);
    }

    private void updateCategory(Category category, CategoryDTO categoryDTO) {
        if(categoryDTO.getName() != null) {
            category.setName(categoryDTO.getName());
        }
        if(categoryDTO.getParentId() != null) {
            category.setParentId(categoryDTO.getParentId());
        }
    }
}
