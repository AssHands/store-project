package com.ak.store.catalogueSagaWorker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductStatus {
    IN_PROGRESS("IN_PROGRESS"),
    ACTIVE("ACTIVE"),
    DELETED("DELETED");

    private final String value;
}
