package com.ak.store.kafka.storekafkastarter.model.event.catalogue.characteristic;

import com.ak.store.kafka.storekafkastarter.KafkaEvent;
import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.characteristic.CharacteristicPayloadSnapshot;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CharacteristicCreatedEvent implements KafkaEvent {
    private UUID eventId;

    private CharacteristicPayloadSnapshot payload;
}