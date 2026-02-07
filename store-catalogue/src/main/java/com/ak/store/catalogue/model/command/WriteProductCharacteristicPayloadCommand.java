package com.ak.store.catalogue.model.command;

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
public class WriteProductCharacteristicPayloadCommand {
    private Long productId;
    private List<WriteProductCharacteristicCommand> addCharacteristics;
    private List<WriteProductCharacteristicCommand> removeCharacteristics;
}
