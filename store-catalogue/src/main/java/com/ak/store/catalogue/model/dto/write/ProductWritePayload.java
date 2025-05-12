package com.ak.store.catalogue.model.dto.write;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductWritePayload {
    @Builder.Default
    ProductWriteDTO product = new ProductWriteDTO();

    @Builder.Default
    List<ProductCharacteristicWriteDTO> createProductCharacteristics = new ArrayList<>();

    @Builder.Default
    List<ProductCharacteristicWriteDTO> updateProductCharacteristics = new ArrayList<>();

    @Builder.Default
    List<Long> deleteProductCharacteristicIds = new ArrayList<>();
}
