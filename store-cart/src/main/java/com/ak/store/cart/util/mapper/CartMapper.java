package com.ak.store.cart.util.mapper;

import com.ak.store.common.model.cart.document.CartDocument;
import com.ak.store.common.model.cart.view.CartView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CartMapper {
    CartView toCartView(CartDocument cartDocument);
}
