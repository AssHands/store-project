package com.ak.store.catalogue.util.mapper;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.ProductCharacteristic;
import com.ak.store.common.model.catalogue.dto.ProductCharacteristicDTO;
import com.ak.store.common.model.catalogue.dto.ProductDTO;
import com.ak.store.common.model.catalogue.form.ProductForm;
import com.ak.store.common.model.catalogue.view.ProductCharacteristicView;
import com.ak.store.common.model.catalogue.view.ProductPoorView;
import com.ak.store.common.model.catalogue.dto.ProductPriceDTO;
import com.ak.store.common.model.catalogue.view.ProductRichView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "characteristics", source = "characteristics")
    @Mapping(target = "images", source = "images")
    ProductDTO toProductDTO(Product product);

    @Mapping(target = "category", expression = "java(category)")
    @Mapping(target = "characteristics", ignore = true)
    @Mapping(target = "id", ignore = true)
    Product toProduct(ProductForm productForm, Category category);

    ProductPoorView toProductPoorView(Product product);

    @Mapping(target = "price", source = "currentPrice")
    ProductPriceDTO toProductPriceDTO(Product product);

    ProductRichView toProductRichView(Product product);

    @Mapping(target = "id", source = "characteristic.id")
    ProductCharacteristicView toProductCharacteristicView(ProductCharacteristic productCharacteristic);

    @Mapping(target = "id", source = "characteristic.id")
    ProductCharacteristicDTO toProductCharacteristicDTO(ProductCharacteristic productCharacteristic);
}
