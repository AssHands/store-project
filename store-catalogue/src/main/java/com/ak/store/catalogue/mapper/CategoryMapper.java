package com.ak.store.catalogue.mapper;

import com.ak.store.catalogue.model.command.WriteCategoryCharacteristicCommand;
import com.ak.store.catalogue.model.command.WriteCategoryCommand;
import com.ak.store.catalogue.model.dto.CategoryDTO;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.form.WriteCategoryCharacteristicForm;
import com.ak.store.catalogue.model.form.WriteCategoryForm;
import com.ak.store.catalogue.model.view.CategoryTreeView;
import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.category.CategorySnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CategoryMapper {
    Category toEntity(WriteCategoryCommand command);

    void updateEntity(WriteCategoryCommand command, @MappingTarget Category entity);

    CategoryTreeView toTreeView(CategoryDTO dto);

    CategoryDTO toDTO(Category entity);

    WriteCategoryCommand toWriteCommand(WriteCategoryForm form);

    WriteCategoryCharacteristicCommand toWriteCharacteristicCommand(WriteCategoryCharacteristicForm form);

    CategorySnapshot toSnapshot(Category entity);
}
