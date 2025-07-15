package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.integration.S3Service;
import com.ak.store.catalogue.mapper.ImageMapper;
import com.ak.store.catalogue.mapper.ProductCharacteristicMapper;
import com.ak.store.catalogue.mapper.ProductMapper;
import com.ak.store.catalogue.model.dto.ProductDTO;
import com.ak.store.catalogue.model.dto.write.ImageWriteDTO;
import com.ak.store.catalogue.model.dto.write.ProductWritePayload;
import com.ak.store.catalogue.outbox.OutboxEventService;
import com.ak.store.catalogue.outbox.OutboxEventType;
import com.ak.store.catalogue.service.ImageService;
import com.ak.store.catalogue.service.ProductCharacteristicService;
import com.ak.store.catalogue.service.ProductService;
import com.ak.store.common.snapshot.catalogue.ProductCreationSnapshot;
import com.ak.store.common.snapshot.catalogue.ProductSnapshotPayload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductFacade {
    private final ProductService productService;
    private final ImageService imageService;
    private final ProductCharacteristicService productCharacteristicService;
    private final OutboxEventService productOutboxEventService;
    private final S3Service s3Service;

    private final ProductMapper productMapper;
    private final ImageMapper imageMapper;
    private final ProductCharacteristicMapper productCharacteristicMapper;

    public ProductDTO findOne(Long id) {
        return productService.findOne(id);
    }

    public List<ProductDTO> findAll(List<Long> ids) {
        return productService.findAll(ids);
    }

    public Boolean isExistAll(List<Long> ids) {
        return productService.isExistAll(ids);
    }

    public Boolean isAvailableAll(List<Long> ids) {
        return productService.isAvailableAll(ids);
    }

    @Transactional
    public Long createOne(ProductWritePayload request) {
        var product = productService.createOne(request.getProduct());
        var productCharacteristics =
                productCharacteristicService.createAll(product.getId(), request.getCreateCharacteristics());

        var snapshot = ProductCreationSnapshot.builder()
                .payload(ProductSnapshotPayload.builder()
                        .product(productMapper.toProductSnapshot(product))
                        .characteristics(productCharacteristicMapper.toProductCharacteristicSnapshot(productCharacteristics))
                        .build())
                .build();

        productOutboxEventService.createOne(snapshot, OutboxEventType.PRODUCT_CREATION);
        return product.getId();
    }

    @Transactional
    public Long updateOne(Long id, ProductWritePayload request) {
        var product = productService.updateOne(id, request.getProduct());

        productCharacteristicService.createAll(id, request.getCreateCharacteristics());
        productCharacteristicService.updateAll(id, request.getUpdateCharacteristics());
        var productCharacteristics =
                productCharacteristicService.deleteAllByCharacteristicIds(id, request.getDeleteCharacteristicIds());

        var images = imageService.findAll(product.getId());

        var snapshot = ProductSnapshotPayload.builder()
                .product(productMapper.toProductSnapshot(product))
                .characteristics(productCharacteristicMapper.toProductCharacteristicSnapshot(productCharacteristics))
                .images(imageMapper.toImageSnapshot(images))
                .build();

        productOutboxEventService.createOne(snapshot, OutboxEventType.PRODUCT_UPDATED);
        return product.getId();
    }

    @Transactional
    //todo добавить snapshot вместо строки?
    public void deleteOne(Long id) {
        var product = productService.deleteOne(id);

        String snapshot = product.getId().toString();

        productOutboxEventService.createOne(snapshot, OutboxEventType.PRODUCT_DELETED);
    }

    @Transactional
    public Long saveOrUpdateAllImage(ImageWriteDTO request) {
        var processedProductImages = imageService.saveOrUpdateAllImage(request);

        var productCharacteristics = productCharacteristicService.findAll(request.getProductId());
        var images = imageService.findAll(request.getProductId());
        var product = productService.findOne(request.getProductId());

        var snapshot = ProductSnapshotPayload.builder()
                .product(productMapper.toProductSnapshot(product))
                .characteristics(productCharacteristicMapper.toProductCharacteristicSnapshot(productCharacteristics))
                .images(imageMapper.toImageSnapshot(images))
                .build();

        productOutboxEventService.createOne(snapshot, OutboxEventType.PRODUCT_UPDATED);

        try {
            s3Service.putAllImage(processedProductImages.getImagesForAdd());
            s3Service.deleteAllImage(processedProductImages.getImageKeysForDelete());
        } catch (Exception e) {
            s3Service.compensatePutAllImage(processedProductImages.getImagesForAdd().keySet());
            s3Service.compensateDeleteAllImage(processedProductImages.getImageKeysForDelete());
            throw e;
        }

        return product.getId();
    }
}