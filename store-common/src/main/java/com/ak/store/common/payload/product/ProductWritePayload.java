package com.ak.store.common.payload.product;

import com.ak.store.common.dto.catalogue.others.CharacteristicsWriteDTO;
import com.ak.store.common.dto.catalogue.product.ProductWriteDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWritePayload {
    @JsonProperty("product")
    ProductWriteDTO productWriteDTO;

    @JsonProperty("characteristics")
    CharacteristicsWriteDTO characteristicsWriteDTO;
}