package com.ak.store.common.saga.orderCreation.event.warehouse;

import com.ak.store.common.saga.SagaEvent;
import com.ak.store.common.saga.orderCreation.pojo.warehouse.WarehouseReleaseProductsRequest;
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
public class WarehouseReleaseProductsRequestEvent extends SagaEvent {
    private WarehouseReleaseProductsRequest request;
}
