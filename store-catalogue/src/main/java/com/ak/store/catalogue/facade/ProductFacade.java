package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.integration.S3Service;
import com.ak.store.catalogue.model.dto.ImageDTOnew;
import com.ak.store.catalogue.model.dto.ProductCharacteristicDTOnew;
import com.ak.store.catalogue.model.dto.ProductDTOnew;
import com.ak.store.catalogue.model.dto.write.ProductWritePayload;
import com.ak.store.catalogue.outbox.OutboxTaskService;
import com.ak.store.catalogue.outbox.OutboxTaskType;
import com.ak.store.catalogue.service.ImageService;
import com.ak.store.catalogue.service.ProductCharacteristicService;
import com.ak.store.catalogue.service.ProductService;
import com.ak.store.catalogue.util.mapper.ImageMapper;
import com.ak.store.catalogue.util.mapper.ProductCharacteristicMapper;
import com.ak.store.catalogue.util.mapper.ProductMapper;
import com.ak.store.common.model.catalogue.dto.ProductDTO;
import com.ak.store.common.model.catalogue.dto.ProductPriceDTO;
import com.ak.store.common.model.catalogue.form.ImageForm;
import com.ak.store.common.model.catalogue.snapshot.ProductSnapshotPayload;
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
    private final OutboxTaskService<ProductSnapshotPayload> productOutboxTaskService;
    private final S3Service s3Service;

    private final OutboxTaskService<ProductDTO> productOutboxTaskServiceOld;

    private final ProductMapper productMapper;
    private final ImageMapper imageMapper;
    private final ProductCharacteristicMapper productCharacteristicMapper;

    public ProductDTOnew findOne(Long id) {
        return productService.findOne(id);
    }

    public List<ProductDTOnew> findAll(List<Long> ids) {
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
        //todo проверить синхронизацию продукта и характеристик
        ProductDTOnew product = productService.createOne(request.getProduct());
        List<ProductCharacteristicDTOnew> productCharacteristics =
                productCharacteristicService.createAll(product.getId(), request.getCreateProductCharacteristics());
        List<ImageDTOnew> images = imageService.findAll(product.getId());

        ProductSnapshotPayload snapshot = ProductSnapshotPayload.builder()
                .product(productMapper.toProductSnapshot(product))
                .productCharacteristics(productCharacteristicMapper.toProductCharacteristicSnapshot(productCharacteristics))
                .productImages(imageMapper.toImageSnapshot(images))
                .build();

        productOutboxTaskService.createOneTask(snapshot, OutboxTaskType.PRODUCT_CREATED);
        return product.getId();
    }

    @Transactional
    public Long updateOne(Long id, ProductWritePayload request) {
        ProductDTOnew product = productService.updateOne(id, request.getProduct());

        productCharacteristicService.createAll(id, request.getCreateProductCharacteristics());
        productCharacteristicService.updateAll(id, request.getUpdateProductCharacteristics());
        List<ProductCharacteristicDTOnew> productCharacteristics =
                productCharacteristicService.deleteAll(id, request.getDeleteProductCharacteristicIds());

        List<ImageDTOnew> images = imageService.findAll(product.getId());

        ProductSnapshotPayload snapshot = ProductSnapshotPayload.builder()
                .product(productMapper.toProductSnapshot(product))
                .productCharacteristics(productCharacteristicMapper.toProductCharacteristicSnapshot(productCharacteristics))
                .productImages(imageMapper.toImageSnapshot(images))
                .build();

        productOutboxTaskService.createOneTask(snapshot, OutboxTaskType.PRODUCT_UPDATED);
        return product.getId();
    }

    @Transactional
    public void deleteOne(Long id) {
        List<ProductCharacteristicDTOnew> productCharacteristics = productCharacteristicService.findAll(id);
        List<ImageDTOnew> images = imageService.findAll(id);
        List<String> imageKeys = images.stream()
                .map(ImageDTOnew::getKey)
                .toList();

        ProductDTOnew product = productService.deleteOne(id);
        imageService.deleteAll(id);

        ProductSnapshotPayload snapshot = ProductSnapshotPayload.builder()
                .product(productMapper.toProductSnapshot(product))
                .productCharacteristics(productCharacteristicMapper.toProductCharacteristicSnapshot(productCharacteristics))
                .productImages(imageMapper.toImageSnapshot(images))
                .build();

        productOutboxTaskService.createOneTask(snapshot, OutboxTaskType.PRODUCT_DELETED);

        try {
            s3Service.deleteAllImage(imageKeys);
        } catch (Exception e) {
            s3Service.compensateDeleteAllImage(imageKeys);
            throw e;
        }
    }

    @Transactional
    public Long saveOrUpdateAllImage(ImageForm imageForm) {
        var processedProductImages = productService.saveOrUpdateAllImage(imageForm);
        var product = productService.findOneWithImages(imageForm.getProductId());

        productOutboxTaskServiceOld.createOneTask(productMapper.toProductDTO(product), OutboxTaskType.PRODUCT_UPDATED);

        try {
            s3Service.putAllImage(processedProductImages.getImagesForAdd());
            s3Service.deleteAllImage(processedProductImages.getImageKeysForDelete());
        } catch(Exception e) {
            s3Service.compensatePutAllImage(processedProductImages.getImagesForAdd().keySet());
            s3Service.compensateDeleteAllImage(processedProductImages.getImageKeysForDelete());
            throw e;
        }

        return product.getId();
    }

    public List<ProductPriceDTO> getAllPrice(List<Long> ids) {
        return productService.findAllOld(ids).stream()
                .map(productMapper::toProductPriceDTO)
                .toList();
    }
}
