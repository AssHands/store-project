package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.dto.ImageDTO;
import com.ak.store.catalogue.model.dto.write.ImageWriteDTO;
import com.ak.store.catalogue.model.entity.Image;
import com.ak.store.catalogue.model.pojo.ProcessedImages;
import com.ak.store.catalogue.repository.ImageRepo;
import com.ak.store.catalogue.util.ImageProcessor;
import com.ak.store.catalogue.mapper.ImageMapper;
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
        return imageMapper.toImageDTOnew(findAllByProductId(productId));
    }

    @Transactional
    public List<ImageDTO> deleteAll(Long productId) {
        var images = findAll(productId);
        imageRepo.deleteAllByProductId(productId);
        return images;
    }

    @Transactional
    public ProcessedImages saveOrUpdateAllImage(ImageWriteDTO request) {
        List<ImageDTO> images = findAll(request.getProductId());
        imageValidator.validate(request, images);
        ProcessedImages processedImages = imageProcessor.process(request, images);

        deleteAll(request.getProductId());
        imageRepo.saveAll(imageMapper.toImage(processedImages.getAllImages(), request.getProductId()));
        return processedImages;
    }
}