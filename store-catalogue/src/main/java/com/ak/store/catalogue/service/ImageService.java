package com.ak.store.catalogue.service;

import com.ak.store.catalogue.mapper.ImageMapper;
import com.ak.store.catalogue.model.dto.ImageDTO;
import com.ak.store.catalogue.model.command.WriteImageCommand;
import com.ak.store.catalogue.model.entity.Image;
import com.ak.store.catalogue.model.pojo.ProcessedImages;
import com.ak.store.catalogue.repository.ImageRepo;
import com.ak.store.catalogue.util.ImageProcessor;
import com.ak.store.catalogue.validator.ImageValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepo imageRepo;
    private final ImageMapper imageMapper;
    private final ImageValidator imageValidator;
    private final ImageProcessor imageProcessor;

    private List<Image> findAllByProductId(Long productId) {
        return imageRepo.findAllByProductId(productId);
    }

    public List<ImageDTO> findAll(Long productId) {
        return findAllByProductId(productId).stream()
                .map(imageMapper::toDTO)
                .toList();
    }

    @Transactional
    public List<ImageDTO> deleteAll(Long productId) {
        var images = findAll(productId);
        imageRepo.deleteAllByProductId(productId);
        return images;
    }

    @Transactional
    public ProcessedImages saveOrUpdateAllImage(WriteImageCommand command) {
        List<ImageDTO> images = findAll(command.getProductId());
        imageValidator.validate(command, images);
        ProcessedImages processedImages = imageProcessor.process(command, images);

        deleteAll(command.getProductId());

        imageRepo.saveAll(
                processedImages.getAllImages()
                        .stream()
                        .map(dto -> imageMapper.toEntity(dto, command.getProductId()))
                        .toList()
        );

        return processedImages;
    }
}