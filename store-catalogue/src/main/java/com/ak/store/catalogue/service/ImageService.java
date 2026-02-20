package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.command.WriteImageCommand;
import com.ak.store.catalogue.model.dto.ImageDTO;
import com.ak.store.catalogue.model.pojo.ProcessedImages;
import com.ak.store.catalogue.util.ImageProcessor;
import com.ak.store.catalogue.validator.ImageValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageValidator imageValidator;
    private final ImageProcessor imageProcessor;
    private final ImageMetadataService imageMetadataService;
    private final ImageStorageService imageStorageService;

    public List<ImageDTO> findAll(Long productId) {
        return imageMetadataService.findAll(productId);
    }

    @Transactional
    public ProcessedImages updateAllImage(WriteImageCommand command) {
        var images = imageMetadataService.findAll(command.getProductId());
        imageValidator.validate(command, images);

        var processedImages = imageProcessor.process(command, images);

        imageMetadataService.replaceAll(command.getProductId(), processedImages.getAllImages());
        imageStorageService.sync(processedImages);

        return processedImages;
    }
}
