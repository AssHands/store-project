package com.ak.store.warehouseSagaWorker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InboxEventType {
    RESERVE_PRODUCTS("RESERVE_PRODUCTS"),
    RELEASE_PRODUCTS("RELEASE_PRODUCTS"),

    INVENTORY_CREATION("INVENTORY_CREATION"),
    CANCEL_INVENTORY_CREATION("CANCEL_INVENTORY_CREATION");

    private final String value;
}
