package com.ak.store.emailSender.mapper;

import com.ak.store.common.event.order.OrderCreatedEvent;
import com.ak.store.emailSender.model.dto.OrderCreatedWriteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface EmailMapper {
    OrderCreatedWriteDTO toOrderCreatedWriteDTO(OrderCreatedEvent event);
}
