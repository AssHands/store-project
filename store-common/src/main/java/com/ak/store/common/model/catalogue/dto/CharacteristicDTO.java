package com.ak.store.common.model.catalogue.dto;

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
public class CharacteristicDTO {
    private Long id;
    private String name;
    private Boolean isText;

    @Builder.Default
    private List<RangeValueDTO> rangeValues = new ArrayList<>();

    @Builder.Default
    private List<String> textValues = new ArrayList<>();
}
