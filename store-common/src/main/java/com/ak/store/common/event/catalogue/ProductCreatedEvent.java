package com.ak.store.common.event.catalogue;

import com.ak.store.common.document.ProductDocument;
import com.ak.store.common.model.catalogue.view.ProductRichView;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductCreatedEvent implements ProductEvent {
    private ProductRichView product;
}
