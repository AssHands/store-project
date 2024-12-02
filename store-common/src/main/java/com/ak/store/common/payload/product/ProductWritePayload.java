package com.ak.store.common.payload.product;

import com.ak.store.common.dto.catalogue.others.CharacteristicsWriteDTO;
import com.ak.store.common.dto.catalogue.product.ProductWriteDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductWritePayload {
    @JsonProperty("product")
    ProductWriteDTO productWriteDTO = new ProductWriteDTO();

    @JsonProperty("create_characteristics")
    CharacteristicsWriteDTO createCharacteristics = new CharacteristicsWriteDTO();

    @JsonProperty("update_characteristics")
    CharacteristicsWriteDTO updateCharacteristics = new CharacteristicsWriteDTO();

    @JsonProperty("delete_characteristics")
    CharacteristicsWriteDTO deleteCharacteristics = new CharacteristicsWriteDTO();
}