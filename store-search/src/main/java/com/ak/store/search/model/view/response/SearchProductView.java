package com.ak.store.search.model.view.response;

import com.ak.store.search.model.view.ProductView;
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
public class SearchProductView {
    @Builder.Default
    private List<ProductView> content = new ArrayList<>();

    @Builder.Default
    private List<Object> searchAfter = new ArrayList<>();
}
