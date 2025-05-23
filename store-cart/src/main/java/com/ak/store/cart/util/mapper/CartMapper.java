package com.ak.store.cart.util.mapper;

import com.ak.store.common.model.cart.document.CartDocument;
import com.ak.store.common.model.cart.view.CartView;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CartMapper {
    @Mapping(target = "content", source = "products")
    CartView toCartView(CartDocument cartDocument);
}
