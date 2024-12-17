package com.ak.store.common.payload.product;

import com.ak.store.common.dto.catalogue.product.CharacteristicWriteDTO;
import com.ak.store.common.dto.catalogue.product.ProductWriteDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductWritePayload {
    ProductWriteDTO product = new ProductWriteDTO();

    List<CharacteristicWriteDTO> createCharacteristics = new ArrayList<>();

    List<CharacteristicWriteDTO> updateCharacteristics = new ArrayList<>();

    List<CharacteristicWriteDTO> deleteCharacteristics = new ArrayList<>();
}