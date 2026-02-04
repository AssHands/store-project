package com.ak.store.catalogue.mapper;

import com.ak.store.catalogue.model.dto.ImageDTO;
import com.ak.store.catalogue.model.entity.Image;
import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.ImageSnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ImageMapper {
    ImageDTO toDTO(Image entity);

    @Mapping(target = "product.id", source = "productId")
    Image toEntity(ImageDTO dto, Long productId);

    ImageSnapshot toSnapshot(Image entity);
}
