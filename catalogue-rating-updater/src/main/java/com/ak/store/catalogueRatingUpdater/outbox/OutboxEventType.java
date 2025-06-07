package com.ak.store.catalogueRatingUpdater.outbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutboxEventType {
    PRODUCT_RATING_UPDATED("PRODUCT_RATING_UPDATED");

    private String type;
}