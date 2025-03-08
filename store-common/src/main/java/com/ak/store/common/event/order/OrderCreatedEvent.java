package com.ak.store.common.event.order;

import com.ak.store.common.model.order.dto.OrderProductDTO;
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
public class OrderCreatedEvent implements OrderEvent {
    private Long orderId;
    private String consumerEmail;
    private Integer totalPrice;

    @Builder.Default
    private List<OrderProductDTO> orderProducts = new ArrayList<>();
}
