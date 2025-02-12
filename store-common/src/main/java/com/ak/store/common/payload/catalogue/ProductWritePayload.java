package com.ak.store.common.payload.catalogue;

import com.ak.store.common.model.catalogue.dto.ProductCharacteristicDTO;
import com.ak.store.common.model.catalogue.dto.ProductDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductWritePayload {
    @Valid
    ProductDTO product = new ProductDTO();

    @Builder.Default
    Set<@Valid ProductCharacteristicDTO> createCharacteristics = new HashSet<>();

    @Builder.Default
    Set<@Valid ProductCharacteristicDTO> updateCharacteristics = new HashSet<>();

    @Builder.Default
    Set<@Valid ProductCharacteristicDTO> deleteCharacteristics = new HashSet<>();
}