package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.model.command.WriteImageCommand;
import com.ak.store.catalogue.service.ImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImageFacade {
    private final ImageService imageService;

    @Transactional
    public Long updateAllImage(WriteImageCommand command) {
        var processedProductImages = imageService.updateAllImage(command);

//        var productCharacteristics = productCharacteristicService.findAll(command.getProductId());
//        var images = imageService.findAll(command.getProductId());
//        var product = productService.findOne(command.getProductId());
//        var snapshot = ProductSnapshotPayload.builder()
//                .product(productMapper.toSnapshot(product))
//                .characteristics(productCharacteristicMapper.toSnapshot(productCharacteristics))
//                .images(imageMapper.toImageSnapshot(images))
//                .build();
//
//        productOutboxEventService.createOne(snapshot, OutboxEventType.PRODUCT_UPDATED);

        return command.getProductId();
    }
}
