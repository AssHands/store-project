package com.ak.store.catalogue.product.service.image;

import com.ak.store.catalogue.product.mapper.ImageMapper;
import com.ak.store.catalogue.model.dto.ImageDTO;
import com.ak.store.catalogue.model.entity.Image;
import com.ak.store.catalogue.repository.ImageRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageMetadataService {
    private final ImageRepo imageRepo;
    private final ImageMapper imageMapper;

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
    public void replaceAll(Long productId, List<ImageDTO> allImages) {
        deleteAll(productId);
        imageRepo.saveAll(allImages.stream()
                .map(dto -> imageMapper.toEntity(dto, productId))
                .toList());
    }
}
