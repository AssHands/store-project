package com.ak.store.catalogue.service;

import com.ak.store.catalogue.mapper.ImageMapper;
import com.ak.store.catalogue.model.dto.ImageDTO;
import com.ak.store.catalogue.model.command.WriteImageCommand;
import com.ak.store.catalogue.model.entity.Image;
import com.ak.store.catalogue.model.pojo.ProcessedImages;
import com.ak.store.catalogue.repository.ImageFileRepo;
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
    private final ImageFileRepo imageFileRepo;

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

    //todo переписать валидатор и структуру метода
    @Transactional
    public ProcessedImages updateAllImage(WriteImageCommand command) {
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

        //todo перенести в отдельный класс сервис, который взаимодействует с s3 хранилищем
        try {
            imageFileRepo.addAllImage(processedImages.getImagesForAdd());
            imageFileRepo.deleteAllImage(processedImages.getImageKeysForDelete());
        } catch (Exception e) {
            imageFileRepo.compensateAddAllImage(processedImages.getImagesForAdd().keySet());
            imageFileRepo.compensateDeleteAllImage(processedImages.getImageKeysForDelete());
            throw e;
        }

        return processedImages;
    }
}