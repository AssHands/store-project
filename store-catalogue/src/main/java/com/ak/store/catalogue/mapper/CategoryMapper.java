package com.ak.store.catalogue.mapper;

import com.ak.store.catalogue.model.dto.CategoryDTO;
import com.ak.store.catalogue.model.dto.write.CategoryWriteDTO;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.form.CategoryForm;
import com.ak.store.catalogue.model.view.CategoryTreeView;
import com.ak.store.common.model.catalogue.snapshot.CategorySnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CategoryMapper {
    Category toCategory(CategoryWriteDTO c);

    CategoryTreeView toCategoryTreeView(CategoryDTO c);

    CategoryDTO toCategoryDTO(Category c);
    List<CategoryDTO> toCategoryDTO(List<Category> c);

    CategoryWriteDTO toCategoryWriteDTO(CategoryForm c);

    CategorySnapshot toCategorySnapshot(CategoryDTO category);
}
