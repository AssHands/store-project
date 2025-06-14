package com.ak.store.common.saga.orderCreation.event.warehouse;

import com.ak.store.common.saga.SagaEvent;
import com.ak.store.common.saga.orderCreation.pojo.warehouse.WarehouseReserveProductsRequest;
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
public class WarehouseReserveProductsRequestEvent extends SagaEvent {
    private WarehouseReserveProductsRequest request;
}
