package com.ak.store.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED"),
    FAILED("FAILED");

    private String value;
}
