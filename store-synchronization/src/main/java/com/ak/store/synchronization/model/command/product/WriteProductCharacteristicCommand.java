package com.ak.store.synchronization.model.command.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WriteProductCharacteristicCommand {
    private Long id;

    private String textValue;

    private Integer numericValue;
}
