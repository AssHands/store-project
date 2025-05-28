package com.ak.store.orderOutbox.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutboxEventType {
    ORDER_CREATED("ORDER_CREATED");

    private String type;
}