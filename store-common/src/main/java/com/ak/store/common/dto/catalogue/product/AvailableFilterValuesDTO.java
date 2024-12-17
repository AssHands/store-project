package com.ak.store.common.dto.catalogue.product;

import com.ak.store.common.dto.catalogue.others.nested.RangeValueDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AvailableFilterValuesDTO {
    private Long characteristicId;

    private String characteristicName;

    private List<RangeValueDTO> rangeValues = new ArrayList<>();

    private List<String> textValues = new ArrayList<>();
}