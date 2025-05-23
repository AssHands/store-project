package com.ak.store.order.mapper;

import com.ak.store.order.model.dto.OrderDTO;
import com.ak.store.order.model.dto.OrderDTOPayload;
import com.ak.store.order.model.dto.OrderProductDTO;
import com.ak.store.order.model.dto.write.OrderWriteDTO;
import com.ak.store.order.model.entity.Order;
import com.ak.store.order.model.entity.OrderProduct;
import com.ak.store.order.model.form.OrderForm;
import com.ak.store.order.model.view.OrderView;
import com.ak.store.order.model.view.OrderViewPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface OrderMapper {
    OrderDTO toOrderDTO(Order o);

    @Mapping(target = "orderId", source = "order.id")
    OrderProductDTO toOrderProductDTO(OrderProduct op);

    @Mapping(target = "order", source = "o")
    @Mapping(target = "products", source = "o.products")
    OrderDTOPayload toOrderDTOPayload(Order o);

    List<OrderDTOPayload> toOrderDTOPayload(List<Order> o);

    List<OrderViewPayload> toOrderViewPayload(List<OrderDTOPayload> o);

    OrderWriteDTO toOrderWriteDTO(OrderForm o);
}