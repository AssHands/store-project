package com.ak.store.common.model.order.snapshot;

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
public class OrderSnapshot {
    private Long id;

    //todo переместить в event. email не имеет отношения к заказу
    private String userEmail;

    private Integer totalPrice;

    @Builder.Default
    private Map<Long, Integer> productAmount = new HashMap<>();
}
