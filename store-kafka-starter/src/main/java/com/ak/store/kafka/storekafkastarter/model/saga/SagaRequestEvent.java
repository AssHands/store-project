package com.ak.store.kafka.storekafkastarter.model.saga;

import com.ak.store.kafka.storekafkastarter.KafkaEvent;
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
public class SagaRequestEvent<T> implements KafkaEvent {
    private UUID sagaId;

    private String sagaName;

    private UUID stepId;

    private String stepName;

    private T request;
}
