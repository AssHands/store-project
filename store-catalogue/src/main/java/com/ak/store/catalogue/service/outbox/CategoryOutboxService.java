package com.ak.store.catalogue.service.outbox;

import com.ak.store.catalogue.mapper.CategoryMapper;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.CategoryCharacteristic;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.outbox.OutboxEventService;
import com.ak.store.catalogue.outbox.OutboxEventType;
import com.ak.store.catalogue.repository.CategoryRepo;
import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.category.CategoryPayloadSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryOutboxService {
    private final CategoryRepo categoryRepo;
    private final CategoryMapper categoryMapper;
    private final OutboxEventService outboxEventService;

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveCreatedEvent(Long id) {
        var category = findOne(id);

        var snapshot = CategoryPayloadSnapshot.builder()
                .category(categoryMapper.toSnapshot(category))
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.CATEGORY_CREATED);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveUpdatedEvent(Long id) {
        var category = findOne(id);

        var snapshot = CategoryPayloadSnapshot.builder()
                .category(categoryMapper.toSnapshot(category))
                .characteristics(category.getCharacteristics().stream()
                        .map(CategoryCharacteristic::getCharacteristic)
                        .map(Characteristic::getId)
                        .toList())
                .build();

        outboxEventService.createOne(snapshot, OutboxEventType.CATEGORY_UPDATED);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveDeletedEvent(Long id) {
        var snapshot = id.toString();

        outboxEventService.createOne(snapshot, OutboxEventType.CATEGORY_DELETED);
    }

    private Category findOne(Long id) {
        return categoryRepo.findOneFullById(id)
                .orElseThrow(() -> new RuntimeException("category not found"));
    }
}
