package com.ak.store.catalogue.service;

import com.ak.store.catalogue.mapper.CategoryMapper;
import com.ak.store.catalogue.model.command.WriteCategoryCharacteristicCommand;
import com.ak.store.catalogue.model.command.WriteCategoryCommand;
import com.ak.store.catalogue.model.dto.CategoryDTO;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.CategoryCharacteristic;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.repository.CategoryRepo;
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
    private final CategoryServiceValidator categoryServiceValidator;

    private Category findOneById(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    private Category findOneFullById(Long id) {
        return categoryRepo.findOneFullById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public List<CategoryDTO> findAll() {
        return categoryRepo.findAll().stream()
                .map(categoryMapper::toDTO)
                .toList();
    }

    @Transactional
    public CategoryDTO createOne(WriteCategoryCommand command) {
        categoryServiceValidator.validateCreate(command);
        var category = categoryRepo.save(categoryMapper.toEntity(command));
        return categoryMapper.toDTO(category);
    }

    @Transactional
    public CategoryDTO updateOne(WriteCategoryCommand command) {
        var category = findOneById(command.getId());
        categoryServiceValidator.validateUpdate(command);

        categoryMapper.updateEntity(command, category);
        return categoryMapper.toDTO(categoryRepo.save(category));
    }

    @Transactional
    public CategoryDTO deleteOne(Long id) {
        var category = findOneById(id);
        categoryServiceValidator.validateDelete(id);

        categoryRepo.delete(category);
        return categoryMapper.toDTO(category);
    }

    @Transactional
    public CategoryDTO addOneCharacteristic(WriteCategoryCharacteristicCommand command) {
        var category = findOneFullById(command.getCategoryId());
        categoryServiceValidator.validateAddCharacteristic(command.getCategoryId(), command.getCharacteristicId());

        category.getCharacteristics().add(
                CategoryCharacteristic.builder()
                        .category(category)
                        .characteristic(Characteristic.builder()
                                .id(command.getCharacteristicId())
                                .build())
                        .build()
        );

        return categoryMapper.toDTO(categoryRepo.save(category));
    }

    @Transactional
    public CategoryDTO removeOneCharacteristic(WriteCategoryCharacteristicCommand command) {
        var category = findOneFullById(command.getCategoryId());
        categoryServiceValidator.validateRemoveCharacteristic(command.getCategoryId(), command.getCharacteristicId());

        category.getCharacteristics().removeIf(cc ->
                cc.getCharacteristic().getId().equals(command.getCharacteristicId())
        );

        return categoryMapper.toDTO(categoryRepo.save(category));
    }
}
