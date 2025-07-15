package com.ak.store.SynchronizationSagaWorker.model.dto.write;

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
public class ProductWriteDTOPayload {
    private ProductWriteDTO product;

    private List<ProductCharacteristicWriteDTO> characteristics;

    private List<ImageWriteDTO> images;
}
