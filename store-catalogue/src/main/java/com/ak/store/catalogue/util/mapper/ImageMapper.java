package com.ak.store.catalogue.util.mapper;

import com.ak.store.catalogue.model.dto.ImageDTOnew;
import com.ak.store.catalogue.model.entity.Image;
import com.ak.store.common.model.catalogue.snapshot.ProductImageSnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ImageMapper {
    ImageDTOnew toImageDTOnew(Image i);
    List<ImageDTOnew> toImageDTOnew(List<Image> i);

    ProductImageSnapshot toImageSnapshot(ImageDTOnew i);
    List<ProductImageSnapshot> toImageSnapshot(List<ImageDTOnew> i);
}
