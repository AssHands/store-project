package com.ak.store.common.saga.orderCreation.event.payment;

import com.ak.store.common.saga.SagaEvent;
import com.ak.store.common.saga.orderCreation.pojo.payment.PaymentReserveFundsResponse;
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
public class PaymentReserveFundsResponseEvent extends SagaEvent {
    private PaymentReserveFundsResponse response;

    private SagaStatus status;
}