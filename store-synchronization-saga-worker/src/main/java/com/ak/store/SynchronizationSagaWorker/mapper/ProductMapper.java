package com.ak.store.SynchronizationSagaWorker.mapper;

import com.ak.store.SynchronizationSagaWorker.model.command.WriteProductPayloadCommand;
import com.ak.store.SynchronizationSagaWorker.model.document.Product;
import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.product.ProductSnapshotPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapper {
    WriteProductPayloadCommand toWritePayloadCommand(ProductSnapshotPayload snapshot);

    @Mapping(target = ".", source = "product")
    @Mapping(target = "characteristics", source = "characteristics")
    @Mapping(target = "images", source = "images")
    Product toDocument(WriteProductPayloadCommand command);


}
