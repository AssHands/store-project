package com.ak.store.cart.mapper;

import com.ak.store.cart.model.document.Cart;
import com.ak.store.cart.model.dto.CartDTO;
import com.ak.store.cart.model.view.CartView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CartMapper {

    CartView toCartView(CartDTO c);

    CartDTO toCartDTO(Cart c);
}