package com.ak.store.common.payload.catalogue;

import com.ak.store.common.model.catalogue.form.ProductCharacteristicForm;
import com.ak.store.common.model.catalogue.form.ProductForm;
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
    @Builder.Default
    ProductForm product = new ProductForm();

    @Builder.Default
    Set<@Valid ProductCharacteristicForm> createCharacteristics = new HashSet<>();

    @Builder.Default
    Set<@Valid ProductCharacteristicForm> updateCharacteristics = new HashSet<>();

    @Builder.Default
    Set<@Valid ProductCharacteristicForm> deleteCharacteristics = new HashSet<>();
}