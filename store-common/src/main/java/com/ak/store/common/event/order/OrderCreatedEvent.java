package com.ak.store.common.event.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderCreatedEvent implements OrderEvent {
    private Long orderId;

    private String consumerEmail;

    @Builder.Default
    private Map<Long, Integer> productAmountMap = new HashMap<>();
}
