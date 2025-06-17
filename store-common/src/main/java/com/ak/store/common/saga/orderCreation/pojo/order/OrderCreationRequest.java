package com.ak.store.common.saga.orderCreation.pojo.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderCreationRequest {
    private Long orderId;

    private UUID userId;

    private Integer totalPrice;

    @Builder.Default
    private Map<Long, Integer> productAmount = new HashMap<>();
}
