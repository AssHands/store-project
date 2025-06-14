package com.ak.store.common.saga.orderCreation.event.warehouse;

import com.ak.store.common.saga.SagaEvent;
import com.ak.store.common.saga.orderCreation.pojo.warehouse.WarehouseReserveProductsResponse;
import com.ak.store.common.saga.SagaStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WarehouseReleaseProductsResponseEvent extends SagaEvent {
    private WarehouseReserveProductsResponse response;

    private SagaStatus status;
}