package com.ak.store.common.model.catalogue.formNew;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductFormPayloadNew {
    @Valid
    @Builder.Default
    ProductFormNew product = new ProductFormNew();

    @Builder.Default
    List<@Valid ProductCharacteristicFormNew> createCharacteristics = new ArrayList<>();

    @Builder.Default
    List<@Valid ProductCharacteristicFormNew> updateCharacteristics = new ArrayList<>();

    @Builder.Default
    Set<Long> deleteCharacteristicIds = new HashSet<>();
}
