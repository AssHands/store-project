package com.ak.store.emailSender.mapper;

import com.ak.store.common.snapshot.order.OrderCreatedSnapshotPayload;
import com.ak.store.emailSender.model.dto.OrderCreatedWriteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface EmailMapper {

    @Mapping(target = "orderId", source = "p.order.id")
    @Mapping(target = "productAmount", source = "p.order.productAmount")
    @Mapping(target = "userEmail", source = "p.userIdentity.email")
    OrderCreatedWriteDTO toOrderCreatedWriteDTO(OrderCreatedSnapshotPayload p);
}