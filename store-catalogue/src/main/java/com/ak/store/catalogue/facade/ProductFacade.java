package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.model.command.WriteProductCommand;
import com.ak.store.catalogue.model.dto.ProductDTO;
import com.ak.store.catalogue.outbox.OutboxEventService;
import com.ak.store.catalogue.outbox.OutboxEventType;
import com.ak.store.catalogue.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductFacade {
    private final ProductService productService;
    private final OutboxEventService productOutboxEventService;

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
    public Long createOne(WriteProductCommand command) {
        var product = productService.createOne(command);

//        var snapshot = ProductCreationSnapshot.builder()
//                .payload(ProductSnapshotPayload.builder()
//                        .product(productMapper.toSnapshot(product))
//                        .build())
//                .build();
//
//        productOutboxEventService.createOne(snapshot, OutboxEventType.PRODUCT_CREATION);
        return product.getId();
    }

    @Transactional
    public Long updateOne(WriteProductCommand command) {
        var product = productService.updateOne(command);

//        var images = imageService.findAll(product.getId());
//
//        var snapshot = ProductSnapshotPayload.builder()
//                .product(productMapper.toSnapshot(product))
//                .characteristics(productCharacteristicMapper.toSnapshot(productCharacteristics))
//                .images(imageMapper.toImageSnapshot(images))
//                .build();
//
//        productOutboxEventService.createOne(snapshot, OutboxEventType.PRODUCT_UPDATED);
        return product.getId();
    }

    @Transactional
    //todo добавить snapshot вместо строки?
    public void deleteOne(Long id) {
        var product = productService.deleteOne(id);

        String snapshot = product.getId().toString();

        productOutboxEventService.createOne(snapshot, OutboxEventType.PRODUCT_DELETED);
    }
}