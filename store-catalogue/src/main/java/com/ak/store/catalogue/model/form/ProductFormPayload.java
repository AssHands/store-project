package com.ak.store.catalogue.model.form;

import com.ak.store.catalogue.model.annotation.UniqueElements;
import com.ak.store.catalogue.model.validationGroup.Create;
import com.ak.store.catalogue.model.validationGroup.Update;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductFormPayload {
    @Valid
    @Builder.Default
    ProductForm product = new ProductForm();

    @UniqueElements(groups = {Create.class, Update.class})
    @Builder.Default
    List<@Valid ProductCharacteristicForm> createCharacteristics = new ArrayList<>();

    @UniqueElements(groups = {Create.class, Update.class})
    @Builder.Default
    List<@Valid ProductCharacteristicForm> updateCharacteristics = new ArrayList<>();

    @Builder.Default
    List<Long> deleteCharacteristicIds = new ArrayList<>();
}