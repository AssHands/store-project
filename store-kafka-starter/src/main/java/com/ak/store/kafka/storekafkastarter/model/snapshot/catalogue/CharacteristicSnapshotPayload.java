package com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue;

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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CharacteristicSnapshotPayload {
    private CharacteristicSnapshot characteristic;

    @Builder.Default
    private List<String> textValues = new ArrayList<>();

    @Builder.Default
    private List<NumericValueSnapshot> numericValues = new ArrayList<>();
}