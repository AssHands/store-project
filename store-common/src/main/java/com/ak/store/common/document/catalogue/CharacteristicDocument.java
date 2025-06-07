package com.ak.store.common.document.catalogue;

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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CharacteristicDocument {
    private Long id;

    private String name;

    private Boolean isText;

    private List<String> textValues = new ArrayList<>();

    private List<NumericValueDocument> numericValues = new ArrayList<>();
}