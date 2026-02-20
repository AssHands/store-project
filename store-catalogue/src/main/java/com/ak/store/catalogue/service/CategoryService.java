package com.ak.store.catalogue.service;

import com.ak.store.catalogue.exception.NotFoundException;
import com.ak.store.catalogue.mapper.CategoryMapper;
import com.ak.store.catalogue.model.command.WriteCategoryCharacteristicCommand;
import com.ak.store.catalogue.model.command.WriteCategoryCommand;
import com.ak.store.catalogue.model.dto.CategoryDTO;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.CategoryCharacteristic;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.repository.CategoryRepo;
import com.ak.store.catalogue.validator.CategoryValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepo categoryRepo;
    private final CategoryValidator categoryValidator;

    private Category findOneById(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: id=" + id));
    }

    private Category findOneFullById(Long id) {
        return categoryRepo.findOneFullById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: id=" + id));
    }

    public List<CategoryDTO> findAll() {
        return categoryRepo.findAll().stream()
                .map(categoryMapper::toDTO)
                .toList();
    }

    @Transactional
    public CategoryDTO createOne(WriteCategoryCommand command) {
        categoryValidator.validateCreate(command);

        var category = categoryRepo.save(categoryMapper.toEntity(command));

        return categoryMapper.toDTO(category);
    }

    @Transactional
    public CategoryDTO updateOne(WriteCategoryCommand command) {
        categoryValidator.validateUpdate(command);

        var category = findOneById(command.getId());
        categoryMapper.updateEntity(command, category);

        return categoryMapper.toDTO(categoryRepo.save(category));
    }

    @Transactional
    public CategoryDTO deleteOne(Long id) {
        categoryValidator.validateDelete(id);

        var category = findOneById(id);
        categoryRepo.delete(category);

        return categoryMapper.toDTO(category);
    }

    @Transactional
    public CategoryDTO addOneCharacteristic(WriteCategoryCharacteristicCommand command) {
        categoryValidator.validateAddOneCharacteristic(command);

        var category = findOneFullById(command.getCategoryId());

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
        categoryValidator.validateRemoveOneCharacteristic(command);

        var category = findOneFullById(command.getCategoryId());

        category.getCharacteristics().removeIf(cc ->
                cc.getCharacteristic().getId().equals(command.getCharacteristicId())
        );

        return categoryMapper.toDTO(categoryRepo.save(category));
    }
}
