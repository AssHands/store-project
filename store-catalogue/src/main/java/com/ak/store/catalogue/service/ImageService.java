package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.dto.ImageDTOnew;
import com.ak.store.catalogue.model.entity.Image;
import com.ak.store.catalogue.repository.ImageRepo;
import com.ak.store.catalogue.util.mapper.ImageMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepo imageRepo;
    private final ImageMapper imageMapper;


    private List<Image> findAllByProductId(Long productId) {
        return imageRepo.findAllByProductId(productId);
    }

    public List<ImageDTOnew> findAll(Long productIde) {
        return imageMapper.toImageDTOnew(findAllByProductId(productIde));
    }

    @Transactional
    public void deleteAll(Long productId) {
        imageRepo.deleteAllByProductId(productId);
    }
}
