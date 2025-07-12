package com.ak.store.catalogueSagaWorker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  OutboxEventType {
    ADD_PRODUCT_GRADE("ADD_PRODUCT_GRADE"),
    REMOVE_PRODUCT_GRADE("REMOVE_PRODUCT_GRADE"),

    UPDATE_PRODUCT_GRADE("UPDATE_PRODUCT_GRADE"),
    CANCEL_UPDATE_PRODUCT_GRADE("CANCEL_UPDATE_PRODUCT_GRADE"),

    DELETE_PRODUCT_GRADE("DELETE_PRODUCT_GRADE"),
    CANCEL_DELETE_PRODUCT_GRADE("CANCEL_DELETE_PRODUCT_GRADE");

    private final String value;
}
