package com.ak.sotre.orderOutbox.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutboxEventType {
    ORDER_CREATED("ORDER_CREATED");

    private String type;
}