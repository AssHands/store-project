package com.ak.store.catalogue.mapper;

import com.ak.store.catalogue.model.dto.ImageDTO;
import com.ak.store.catalogue.model.entity.Image;
import com.ak.store.common.snapshot.catalogue.ProductImageSnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ImageMapper {
    ImageDTO toImageDTOnew(Image i);

    @Mapping(target = "product.id", source = "productId")
    Image toImage(ImageDTO i, Long productId);
    default List<Image> toImage(List<ImageDTO> i, Long productId) {
        if (i == null) {
            return null;
        }

        return i.stream()
                .map(v -> toImage(v, productId))
                .toList();
    }

    List<ImageDTO> toImageDTOnew(List<Image> i);

    ProductImageSnapshot toImageSnapshot(ImageDTO i);

    List<ProductImageSnapshot> toImageSnapshot(List<ImageDTO> i);
}
