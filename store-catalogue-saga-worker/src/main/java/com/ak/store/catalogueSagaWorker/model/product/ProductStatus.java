package com.ak.store.catalogueSagaWorker.model.product;

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
