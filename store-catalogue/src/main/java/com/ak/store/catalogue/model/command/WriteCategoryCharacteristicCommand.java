package com.ak.store.catalogue.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WriteCategoryCharacteristicCommand {
    private Long categoryId;
    private Long characteristicId;
}
