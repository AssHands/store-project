package com.ak.store.common.model.order.view;

import com.ak.store.common.model.catalogue.view.ProductPoorView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderProductView {
    private ProductPoorView productPoorView = new ProductPoorView();
    private Integer amount;
    private Integer pricePerOne;
}
