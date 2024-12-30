package com.ak.store.common.dto.search.nested;

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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TextFilter {
    private Long id;

    private String name;

    private List<String> values = new ArrayList<>();
}