package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.dto.CategoryDTO;
import com.ak.store.catalogue.model.dto.write.CategoryWriteDTO;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.CategoryCharacteristic;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.repository.CategoryRepo;
import com.ak.store.catalogue.util.mapper.CategoryMapper;
import com.ak.store.catalogue.validator.service.CategoryServiceValidator;
import jakarta.transaction.Transactional;
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

    private Category findOneById(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    private Category findOneWithCharacteristics(Long id) {
        return categoryRepo.findOneWithCharacteristicsById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    private Category findOneWithRelatedCategories(Long id) {
        //todo если метод закеширован, то кидается другая ошибка
        return categoryRepo.findOneWithRelatedCategoriesById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public List<CategoryDTO> findAll() {
        return categoryMapper.toCategoryDTO(categoryRepo.findAll());
    }

    public CategoryDTO findOne(Long id) {
        return categoryMapper.toCategoryDTO(findOneById(id));
    }

    public List<Long> findAllCharacteristic(Long id) {
        return findOneWithCharacteristics(id).getCharacteristics().stream()
                .map(v -> v.getCharacteristic().getId())
                .toList();
    }

    public List<Long> findAllRelatedCategory(Long id) {
        return findOneWithRelatedCategories(id).getRelatedCategories().stream()
                .map(Category::getId)
                .toList();
    }

    @Transactional
    public CategoryDTO createOne(CategoryWriteDTO request) {
        categoryServiceValidator.validateCreating(request);
        var category = categoryRepo.save(categoryMapper.toCategory(request));
        return categoryMapper.toCategoryDTO(category);
    }

    @Transactional
    public CategoryDTO updateOne(Long id, CategoryWriteDTO request) {
        var category = findOneById(id);
        categoryServiceValidator.validateUpdating(id, request);

        updateOneFromDTO(category, request);
        return categoryMapper.toCategoryDTO(categoryRepo.save(category));
    }

    //todo: make check if product has this category
    @Transactional
    public CategoryDTO deleteOne(Long id) {
        var category = findOneById(id);
        categoryServiceValidator.validateDeleting(id);

        categoryRepo.delete(category);
        return categoryMapper.toCategoryDTO(category);
    }

    @Transactional
    public CategoryDTO addOneCharacteristic(Long id, Long characteristicId) {
        var category = findOneWithCharacteristics(id);
        categoryServiceValidator.validateAddingCharacteristic(id, characteristicId);
        //todo переместить в валидатор
        characteristicService.findOne(characteristicId);

        category.getCharacteristics().add(
                CategoryCharacteristic.builder()
                        .category(category)
                        .characteristic(Characteristic.builder()
                                .id(characteristicId)
                                .build())
                        .build()
        );
        return categoryMapper.toCategoryDTO(categoryRepo.save(category));
    }

    @Transactional
    public CategoryDTO removeOneCharacteristic(Long id, Long characteristicId) {
        var category = findOneWithCharacteristics(id);
        categoryServiceValidator.validateRemovingCharacteristic(id, characteristicId);

        int index = findCharacteristicIndex(category.getCharacteristics(), characteristicId);
        category.getCharacteristics().remove(index);
        return categoryMapper.toCategoryDTO(categoryRepo.save(category));
    }

    @Transactional
    public CategoryDTO addOneRelatedCategory(Long id, Long relatedId) {
        var category = findOneWithRelatedCategories(id);
        categoryServiceValidator.validateAddingRelatedCategory(id, relatedId);

        category.getRelatedCategories().add(Category.builder().id(relatedId).build());
        return categoryMapper.toCategoryDTO(categoryRepo.save(category));
    }

    @Transactional
    public CategoryDTO removeOneRelatedCategory(Long id, Long relatedId) {
        Category category = findOneWithRelatedCategories(id);
        categoryServiceValidator.validateRemovingRelatedCategory(id, relatedId);

        category.getRelatedCategories().remove(Category.builder().id(relatedId).build());
        return categoryMapper.toCategoryDTO(categoryRepo.save(category));
    }

    private void updateOneFromDTO(Category category, CategoryWriteDTO request) {
        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getParentId() != null) {
            category.setParentId(request.getParentId());
        }
    }

    private int findCharacteristicIndex(List<CategoryCharacteristic> categoryCharacteristics, Long id) {
        int index = 0;
        for (var cc : categoryCharacteristics) {
            if (cc.getCharacteristic().getId().equals(id))
                return index;
            index++;
        }
        return -1;
    }
}
