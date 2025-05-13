package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.dto.ImageDTOnew;
import com.ak.store.catalogue.model.entity.Image;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.pojo.ProcessedProductImages;
import com.ak.store.catalogue.repository.ImageRepo;
import com.ak.store.catalogue.util.mapper.ImageMapper;
import com.ak.store.catalogue.validator.ImageValidator;
import com.ak.store.common.model.catalogue.form.ImageForm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ak.store.catalogue.util.ProductImageProcessor.processProductImages;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepo imageRepo;
    private final ImageMapper imageMapper;
    private final ImageValidator imageValidator;


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

    @Transactional
    public ProcessedProductImages saveOrUpdateAllImage(ImageForm imageForm) {
        List<Image> images = findAllByProductId(imageForm.getProductId());
        imageValidator.validate(imageForm, images);
        ProcessedProductImages processedProductImages = processProductImages(imageForm, updatedProduct);

        updatedProduct.getImages().clear();
        updatedProduct.getImages().addAll(processedProductImages.getNewImages());
        productRepo.saveAndFlush(updatedProduct);

        return processedProductImages;
    }
}
