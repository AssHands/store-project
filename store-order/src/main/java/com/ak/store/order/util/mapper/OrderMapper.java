package com.ak.store.order.util.mapper;

import com.ak.store.common.model.order.view.OrderView;
import com.ak.store.order.model.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface OrderMapper {
    OrderView toOrderView(Order order);
}
