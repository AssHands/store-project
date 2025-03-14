package com.ak.store.consumer.util.mapper;

import com.ak.store.common.model.consumer.view.CartView;
import com.ak.store.consumer.model.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CartMapper {

    CartView toCartView(Cart cart);
}
