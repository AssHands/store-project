package com.ak.store.search.model.dto.response;

import com.ak.store.search.model.dto.ProductDTO;
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
public class SearchProductDTO {
    @Builder.Default
    private List<ProductDTO> content = new ArrayList<>();

    @Builder.Default
    private List<Object> searchAfter = new ArrayList<>();
}
