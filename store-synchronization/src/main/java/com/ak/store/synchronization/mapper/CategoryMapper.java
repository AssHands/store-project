package com.ak.store.synchronization.mapper;

import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.category.CategoryPayloadSnapshot;
import com.ak.store.synchronization.model.command.category.WriteCategoryPayloadCommand;
import com.ak.store.synchronization.model.entity.Category;
import com.ak.store.synchronization.model.entity.CategoryCharacteristic;
import com.ak.store.synchronization.model.entity.Characteristic;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CategoryMapper {
    WriteCategoryPayloadCommand toWritePayloadCommand(CategoryPayloadSnapshot snapshot);

    @Mapping(target = "id", source = "category.id")
    @Mapping(target = "name", source = "category.name")
    @Mapping(target = "parentId", source = "category.parentId")
    @Mapping(target = "characteristics", source = "command", qualifiedByName = "toCategoryCharacteristic")
    Category toEntity(WriteCategoryPayloadCommand command);

    @Named("toCategoryCharacteristic")
    default List<CategoryCharacteristic> toCategoryCharacteristic(WriteCategoryPayloadCommand command) {
        return command.getCharacteristics().stream()
                .map(characteristicId -> CategoryCharacteristic.builder()
                        .category(Category.builder()
                                .id(command.getCategory().getId())
                                .build())
                        .characteristic(Characteristic.builder()
                                .id(characteristicId)
                                .build())
                        .build())
                .toList();
    }
}