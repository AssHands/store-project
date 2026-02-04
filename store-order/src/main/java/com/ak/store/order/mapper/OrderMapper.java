package com.ak.store.order.mapper;

import com.ak.store.order.model.dto.OrderDTO;
import com.ak.store.order.model.dto.OrderPayloadDTO;
import com.ak.store.order.model.dto.OrderProductDTO;
import com.ak.store.order.model.command.WriteOrderCommand;
import com.ak.store.order.model.entity.Order;
import com.ak.store.order.model.entity.OrderProduct;
import com.ak.store.order.model.form.OrderForm;
import com.ak.store.order.model.view.OrderPayloadView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface OrderMapper {
    OrderDTO toDTO(Order o);

    @Mapping(target = "orderId", source = "order.id")
    OrderProductDTO toOrderProductDTO(OrderProduct entity);

    @Mapping(target = "order", source = "entity")
    @Mapping(target = "products", source = "entity.products")
    OrderPayloadDTO toPayloadDTO(Order entity);

    OrderPayloadView toPayloadView(OrderPayloadDTO dto);

    WriteOrderCommand toWriteCommand(OrderForm form, UUID userId, String userEmail);
}