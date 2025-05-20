package com.ak.store.catalogue.util.mapper;

import com.ak.store.catalogue.model.dto.ProductDTO;
import com.ak.store.catalogue.model.dto.write.ProductWriteDTO;
import com.ak.store.catalogue.model.dto.write.ProductWritePayload;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.form.ProductFormPayload;
import com.ak.store.common.model.catalogue.snapshot.ProductSnapshot;
import com.ak.store.catalogue.model.view.ProductView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ProductDTO toProductDTO(Product product);
    List<ProductDTO> toProductDTO(List<Product> product);

    ProductView toProductView(ProductDTO p);
    List<ProductView> toProductView(List<ProductDTO> p);

    @Mapping(target = "category.id", source = "categoryId")
    Product toProduct(ProductWriteDTO p);

    ProductSnapshot toProductSnapshot(ProductDTO p);

    ProductWritePayload toProductWritePayload(ProductFormPayload p);
}
