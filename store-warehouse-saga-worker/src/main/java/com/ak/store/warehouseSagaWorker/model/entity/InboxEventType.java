package com.ak.store.warehouseSagaWorker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InboxEventType {
    RESERVE_PRODUCTS("RESERVE_PRODUCTS"),
    RELEASE_PRODUCTS("RELEASE_PRODUCTS");

    private String value;
}
