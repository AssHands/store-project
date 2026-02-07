package com.ak.store.kafka.storekafkastarter.model.event.catalogue.category;

import com.ak.store.kafka.storekafkastarter.model.KafkaEvent;
import com.ak.store.kafka.storekafkastarter.model.snapshot.catalogue.category.CategoryPayloadSnapshot;
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
public class CategoryUpdatedEvent implements KafkaEvent {
    private UUID eventId;

    private CategoryPayloadSnapshot payload;
}
