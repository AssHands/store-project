package com.ak.store.catalogue.util.mapper;

import com.ak.store.catalogue.model.dto.write.ProductWriteDTO;
import com.ak.store.catalogue.model.dto.ProductDTOnew;
import com.ak.store.catalogue.model.dto.write.ProductWritePayload;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.ProductCharacteristic;
import com.ak.store.catalogue.model.entity.Image;
import com.ak.store.common.model.catalogue.formNew.ProductFormPayloadNew;
import com.ak.store.common.model.catalogue.snapshot.ProductSnapshot;
import com.ak.store.common.model.catalogue.viewNew.ProductViewNew;
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

import java.util.List;

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

    //-------------------

    ProductDTOnew toProductDTOnew(Product product);

    List<ProductDTOnew>toProductDTOnew(List<Product> product);

    default List<Long> toProductCharacteristicIds(List<ProductCharacteristic> productCharacteristics) {
        if (productCharacteristics == null || productCharacteristics.isEmpty()) return null;
        return productCharacteristics.stream()
                .map(ProductCharacteristic::getId)
                .toList();
    }

    default List<Long> toProductImageIds(List<Image> images) {
        if (images == null || images.isEmpty()) return null;
        return images.stream()
                .map(Image::getId)
                .toList();
    }

    default Long toCategoryId(Category category) {
        if(category == null) return null;
        return category.getId();
    }

    //-------------------------------

    ProductViewNew toProductViewNew(ProductDTOnew p);

    List<ProductViewNew> toProductViewNew(List<ProductDTOnew> p);

    @Mapping(target = "category.id", source = "categoryId")
    Product toProduct(ProductWriteDTO p);

    ProductSnapshot toProductSnapshot(ProductDTOnew p);

    //todo доработать метод
    ProductWritePayload toProductWritePayload(ProductFormPayloadNew p);
}
