package com.ak.store.catalogue.service.outbox;

import com.ak.store.catalogue.mapper.ImageMapper;
import com.ak.store.catalogue.mapper.ProductCharacteristicMapper;
import com.ak.store.catalogue.mapper.ProductMapper;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.outbox.OutboxEventService;
import com.ak.store.catalogue.outbox.OutboxEventType;
import com.ak.store.catalogue.repository.ProductRepo;
import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.product.ProductSnapshotPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ProductOutboxService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final ImageMapper imageMapper;
    private final ProductCharacteristicMapper pcMapper;
    private final OutboxEventService outboxEventService;

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveCreatedEvent(Long id) {
        var product = findOne(id);

        var snapshot = ProductSnapshotPayload.builder()
                .product(productMapper.toSnapshot(product))
                .images(Collections.emptyList())
                .characteristics(Collections.emptyList())
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.PRODUCT_CREATED);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveUpdatedEvent(Long id) {
        var product = findOne(id);

        var snapshot = ProductSnapshotPayload.builder()
                .product(productMapper.toSnapshot(product))
                .images(product.getImages().stream()
                        .map(imageMapper::toSnapshot)
                        .toList())
                .characteristics(product.getCharacteristics().stream()
                        .map(pcMapper::toSnapshot)
                        .toList())
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.PRODUCT_UPDATED);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveDeletedEvent(Long id) {
        var snapshot = id.toString();
        outboxEventService.createOne(snapshot, OutboxEventType.PRODUCT_DELETED);
    }

    private Product findOne(Long id) {
        return productRepo.findOneFullById(id)
                .orElseThrow(() -> new RuntimeException("product not found"));
    }
}
