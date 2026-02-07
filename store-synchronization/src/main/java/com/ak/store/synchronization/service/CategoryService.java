package com.ak.store.synchronization.service;

import com.ak.store.synchronization.mapper.CategoryMapper;
import com.ak.store.synchronization.model.command.category.WriteCategoryPayloadCommand;
import com.ak.store.synchronization.repository.CategoryRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepo categoryRepo;
    private final CategoryMapper categoryMapper;

    @Transactional
    public void createOne(WriteCategoryPayloadCommand command) {
        categoryRepo.save(categoryMapper.toEntity(command));
    }

    @Transactional
    public void updateOne(WriteCategoryPayloadCommand command) {
        categoryRepo.save(categoryMapper.toEntity(command));
    }

    @Transactional
    public void deleteOne(Long id) {
        categoryRepo.deleteById(id);
    }
}