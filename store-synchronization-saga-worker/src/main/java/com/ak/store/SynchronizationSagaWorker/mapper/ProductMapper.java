package com.ak.store.SynchronizationSagaWorker.mapper;

import com.ak.store.SynchronizationSagaWorker.model.document.Product;
import com.ak.store.SynchronizationSagaWorker.model.dto.ProductSynchronizationRequestPayload;
import com.ak.store.SynchronizationSagaWorker.model.dto.write.ProductWriteDTOPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapper {
    ProductWriteDTOPayload toProductWriteDTOPayload(ProductSynchronizationRequestPayload psrp);

    @Mapping(target = ".", source = "product")
    Product toProduct(ProductWriteDTOPayload pwp);
}
