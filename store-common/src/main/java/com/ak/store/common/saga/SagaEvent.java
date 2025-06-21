package com.ak.store.common.saga;

import com.ak.store.common.event.KafkaEvent;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class SagaEvent implements KafkaEvent {
    private UUID sagaId;

    private String stepName;
}