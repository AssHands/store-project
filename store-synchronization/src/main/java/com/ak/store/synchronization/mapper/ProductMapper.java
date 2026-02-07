package com.ak.store.synchronization.mapper;


import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.product.ProductRatingUpdatedSnapshot;
import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.product.ProductSnapshotPayload;
import com.ak.store.synchronization.model.command.product.WriteProductPayloadCommand;
import com.ak.store.synchronization.model.command.product.WriteProductRatingCommand;
import com.ak.store.synchronization.model.document.Product;
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

    Product toDocument(WriteProductRatingCommand command);

    WriteProductRatingCommand toWriteRatingCommand(ProductRatingUpdatedSnapshot snapshot);
}