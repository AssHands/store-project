package com.ak.store.synchronization.mapper;

import com.ak.store.common.snapshot.catalogue.CategorySnapshot;
import com.ak.store.common.snapshot.catalogue.CategorySnapshotPayload;
import com.ak.store.synchronization.model.entity.Category;
import com.ak.store.synchronization.model.entity.CategoryCharacteristic;
import com.ak.store.synchronization.model.entity.Characteristic;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CategoryMapper {
    @Mapping(target = "id", source = "category.id")
    @Mapping(target = "name", source = "category.name")
    @Mapping(target = "parentId", source = "category.parentId")
    @Mapping(target = "characteristics", source = "csp", qualifiedByName = "toCategoryCharacteristic")
    @Mapping(target = "relatedCategories", source = "csp", qualifiedByName = "toRelatedCategories")
    Category toCategory(CategorySnapshotPayload csp);

    @Named("toCategoryCharacteristic")
    default List<CategoryCharacteristic> toCategoryCharacteristic(CategorySnapshotPayload csp) {
        return csp.getCharacteristics().stream()
                .map(characteristicId -> CategoryCharacteristic.builder()
                        .category(Category.builder()
                                .id(csp.getCategory().getId())
                                .build())
                        .characteristic(Characteristic.builder()
                                .id(characteristicId)
                                .build())
                        .build())
                .toList();
    }

    @Named("toRelatedCategories")
    default Set<Category> toRelatedCategories(CategorySnapshotPayload csp) {
        return csp.getRelatedCategories().stream()
                .map(relatedId -> Category.builder()
                        .id(relatedId)
                        .build())
                .collect(Collectors.toSet());
    }
}