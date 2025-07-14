package com.ak.store.common.kafka.order;

import com.ak.store.common.kafka.KafkaEvent;
import com.ak.store.common.snapshot.order.OrderCreationSnapshot;
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
public class OrderCreationEvent implements KafkaEvent {
    private UUID eventId;

    private OrderCreationSnapshot request;
}