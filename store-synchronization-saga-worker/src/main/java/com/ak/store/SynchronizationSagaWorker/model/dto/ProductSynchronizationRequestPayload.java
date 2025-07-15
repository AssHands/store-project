package com.ak.store.SynchronizationSagaWorker.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductSynchronizationRequestPayload {
    private ProductRequest product;

    private List<ProductCharacteristicRequest> characteristics;

    private List<ProductImageRequest> images;
}
