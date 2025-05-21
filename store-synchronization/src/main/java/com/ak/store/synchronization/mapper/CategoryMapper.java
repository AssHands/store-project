package com.ak.store.synchronization.mapper;

import com.ak.store.common.model.catalogue.document.CategoryDocument;
import com.ak.store.common.model.catalogue.snapshot.CategorySnapshot;
import com.ak.store.common.model.catalogue.snapshot.CategorySnapshotPayload;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CategoryMapper {

    List<CategoryDocument> toCategoryDocument(List<CategorySnapshot> cs);
}
